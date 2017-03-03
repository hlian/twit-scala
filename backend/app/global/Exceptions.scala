package com.originate.global.exceptions

import com.originate.models.WithId

trait ConfigurationException { this: Throwable => }
trait ControllerException { this: Throwable => }
trait JsonBodyException { this: Throwable => }
trait PersistenceException { this: Throwable => }
trait SerializationException { this: Throwable => }
trait ServiceException { this: Throwable => }

trait NoPermissionException { this: Throwable => }
trait NotFoundException { this: Throwable => }
trait ExternalServiceException { this: Throwable => }

case class ConfigurationLoadFailed[T](t: T)
  extends Exception(s"failed to load configuration: $t")
  with ConfigurationException

case class CaseClassShouldNotContainId[T](t: T)
  extends Exception(
    s"Case class $t contains field named 'id' but id should be left for WithId abstraction to handle"
  )
  with JsonBodyException

case class ModelCannotBePersisted[T](table: String, model: T)
  extends Exception(s"could not insert ${model.toString} into $table")
  with PersistenceException

case class ModelNotFound(table: String, id: Int)
  extends Exception(s"expected model with id: $id was not found in table: $table")
  with PersistenceException
