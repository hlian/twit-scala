package com.originate.util

/**
 * A utility object that holds all implicit conversion ops
 */
object Ops {
  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other // scalastyle:off method.name
  }
}
