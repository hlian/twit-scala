package com.originate.support

import com.originate.factories.Factory
import com.originate.models.WithId
import com.originate.persistence.CrudDao

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * CrudDaoBaseSpec is a base trait used to easily test the CRUD functionality of a DAO against a real
 * database. It will ensure that your serialization/deserialization logic works well. It also contains
 * helper functions that make it easy to inject tests for common DB idioms like "query table by field".
 */
abstract class CrudDaoBaseSpec[T](
    nameOfThing: String)
    (implicit factory: Factory[T])
  extends PersistenceFunctionalSpec {

  def make(i: Int): Future[Seq[WithId[T]]]

  def dao: CrudDao[T]

  // scalastyle:off magic.number
  "CRUD operations" - {

    "findById" - {

      s"when $nameOfThing exists" - {

        s"it finds the $nameOfThing" in new BaseContext {
          val tuple = for {
            Seq(thing) <- make(1)
            found <- dao.findById(thing.id)
          } yield (thing, found)

          whenReady(tuple) { case (created, found) =>
            found shouldBe Some(created)
          }
        }

      }

      s"when $nameOfThing doesn't exist" - {

        "returns None" in new BaseContext {
          whenReady(dao.findById(1)) { found =>
            found shouldBe None
          }
        }
      }

    }

    "create" - {

      s"saves a $nameOfThing" in new BaseContext {
        whenReady(make(1)) { instances =>
          instances.size shouldBe 1
        }
      }

      s"returns the created $nameOfThing with its id" in new BaseContext {
        whenComplete(make(1) map (_.head)) {
          case Success(_) =>
          case _ => fail
        }
      }

    }

    "getAll" - {

      s"finds all the ${nameOfThing}s" in new BaseContext {
        whenReady(make(10) flatMap (_ => dao.getAll())) { instances =>
          instances.size shouldBe 10
        }
      }

    }

    "remove" - {

      s"deletes a $nameOfThing" in new BaseContext {
        val deleted = make(1) map (_.head) flatMap (dao.remove)
        whenComplete(deleted) {
          case Success(_) =>
          case Failure(_) => fail
        }
      }

    }

    "update" - {

      s"updates the $nameOfThing" in new BaseContext {
        val createAndUpdate = for {
          original <- make(1) map (_.head)
          changed <- dao.update(factory.alter(original))
        } yield (original, changed)

        whenReady(createAndUpdate) { case (original, changed) =>
          original should not be (changed)
        }
      }

    }

  }

  def testLookupById(
      method: String,
      field: String,
      lookup: => Future[Seq[WithId[T]]],
      modify: T => T): Unit =
    method - {

      s"finds all ${nameOfThing}s with the same $field" in new BaseContext {
        val others = make(2)
        val toFind = Seq.fill(3)(modify(factory.newInstance))
        val created = Future.traverse(toFind)(dao.create)
        val all = for {
          _ <- others
          _ <- created
          all <- lookup
        } yield all

        whenReady(all) { things =>
          things should have length 3
        }
      }

    }

  def testLookupByJoinTable[U](
      methodName: String,
      nameOfThing: String,
      nameOfOtherThing: String,
      joinTableDao: CrudDao[U],
      setJoinedId: (Int, Int, U) => U,
      lookupMethod: Int => Future[Seq[WithId[T]]])
      (implicit joinFactory: Factory[U]): Unit =
    methodName - {

      s"when $nameOfThing links to many other ${nameOfOtherThing}s" - {

        s"gets the list of ${nameOfOtherThing}s" in new BaseContext {
          val joinId = 100

          val query = for {
            models <- make(4)
            joinModels = models map { model =>
              setJoinedId(model.id, joinId, joinFactory.newInstance)
            }
            _ <- Future.traverse(joinModels)(joinTableDao.create(_))
            foundOther <- lookupMethod(joinId)
          } yield (models, foundOther)

          whenReady(query) { case (models, foundOther) =>
            foundOther should contain theSameElementsAs models
          }
        }

      }

    }

  def testLookupByField[F](
      method: String,
      field: String,
      value: F,
      lookup: F => Future[Option[WithId[T]]])
      (modify: (T, F) => T): Unit =
    method - {

      s"$nameOfThing exists" - {

        s"finds the ${nameOfThing} by the specified $field" in new BaseContext {
          val others = make(2)
          val created = dao.create(modify(factory.newInstance, value))
          val all = for {
            _ <- others
            _ <- created
            all <- lookup(value)
          } yield all

          whenReady(all) { thing =>
            thing should not be empty
          }
        }

      }

      s"$nameOfThing doesn't exist" - {

        s"does not find the $nameOfThing" in new BaseContext {
          val others = make(2)
          val all = for {
            _ <- others
            all <- lookup(value)
          } yield all

          whenReady(all) { thing =>
            thing shouldBe empty
          }
        }

      }

    }

  def testLookupByRelation(
      method: String,
      relation: String,
      lookup: Int => Future[Seq[WithId[T]]])
      (modify: (T, Int) => T): Unit =
    method - {

      s"finds all ${nameOfThing}s belonging to a/an $relation" in new BaseContext {
        val others = make(2)
        val id = factory.nextId
        val toFind = Seq.fill(3)(modify(factory.newInstance, id))
        val created = Future.traverse(toFind)(dao.create)
        val all = for {
          _ <- others
          _ <- created
          all <- lookup(id)
        } yield all

        whenReady(all) { things =>
          things should have length 3
        }
      }

    }

  // scalastyle:on magic.number

}

abstract class PgCrudDaoBaseSpec[T](
    nameOfThing: String)
    (implicit factory: Factory[T])
  extends CrudDaoBaseSpec(nameOfThing)
  with PgFunctionalSpec {

  def make(i: Int): Future[Seq[WithId[T]]] = Future.traverse(factory.newInstanceSeq(i))(dao.create)

}

abstract class MySQLCrudDaoBaseSpec[T](
    nameOfThing: String)
    (implicit factory: Factory[T])
  extends CrudDaoBaseSpec(nameOfThing)
  with MySQLFunctionalSpec {

  def make(i: Int): Future[Seq[WithId[T]]] = Future.traverse(factory.newInstanceSeq(i))(dao.create)

}

abstract class H2CrudDaoBaseSpec[T](
    nameOfThing: String)
    (implicit factory: Factory[T])
  extends CrudDaoBaseSpec(nameOfThing)
  with H2FunctionalSpec {

  // H2 is not thread safe, so this ensures that creation of multiple models happens sequentially
  // otherwise you can get id collisions
  def make(i: Int): Future[Seq[WithId[T]]] = {
    val instances = factory.newInstanceSeq(i)
    instances.foldLeft(Future.successful(Seq.empty[WithId[T]])) { case (acc, model) =>
      acc flatMap { list =>
        dao.create(model) map (list :+ _)
      }
    }
  }

}
