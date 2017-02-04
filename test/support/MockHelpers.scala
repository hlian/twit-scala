package com.originate.support

import org.mockito.Matchers._
import org.mockito.Mockito._

import scala.reflect.ClassTag

trait MockHelpers {
  def smartMock[T: ClassTag]: T =
    mock(implicitly[ClassTag[T]].runtimeClass, RETURNS_SMART_NULLS) match {
      case a: T => a
    }
}
