package com.originate.models

import com.originate.global.exceptions.CaseClassShouldNotContainId

import play.api.libs.json.{Format, JsNumber, JsObject, JsResult, JsValue, Json}

import scala.language.implicitConversions

/**
 * WithId adds an id to a model. This separates the two types of data the application sees:
 * * Model: pure data (often from the client)
 * * WithId[Model]: persisted data (often read from the database)
 *
 * Note the implicit `toModel`, which allows you to use `WithId[Model]` values as if
 * they were just `Model`s (ie: `withIdModel.modelField` implicitly converts to
 * `withIdModel.model.modelField`).
 */
case class WithId[T](id: Int, model: T) {
  def copyModel(f: T => T): WithId[T] = copy(model = f(model))
}

object WithId {
  private val Id = "id"

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitConversion"))
  implicit def toModel[T](withId: WithId[T]): T = withId.model

  implicit def format[T](implicit model: Format[T]): Format[WithId[T]] = {
    new Format[WithId[T]] {
      def reads(json: JsValue): JsResult[WithId[T]] = for {
        t <- model.reads(json)
        id <- (json \ Id).validate[Int]
      } yield WithId(id, t)

      def writes(t: WithId[T]): JsValue = {
        val json = model.writes(t.model)
        if ((json \ Id).toOption.nonEmpty) throw new CaseClassShouldNotContainId(t.model)
        Json.obj(Id -> JsNumber(t.id)) ++ json.asOpt[JsObject].getOrElse(Json.obj())
      }
    }
  }
}
