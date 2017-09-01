package com.originate.util

/**
 * A utility object that holds all implicit conversion ops
 */
object Ops {
  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other // scalastyle:off method.name
  }

  implicit final class StringOps(self: String) {
    // Taken form https://github.com/backchatio/scala-inflector/blob/master/src/main/scala/Inflector.scala
    def underscore(): String = {
      val spacesPattern = "[-\\s]".r
      val firstPattern = "([A-Z]+)([A-Z][a-z])".r
      val secondPattern = "([a-z\\d])([A-Z])".r
      val replacementPattern = "$1_$2"
      spacesPattern.replaceAllIn(
        secondPattern.replaceAllIn(
          firstPattern.replaceAllIn(
            self, replacementPattern), replacementPattern), "_").toLowerCase
    }
  }
}
