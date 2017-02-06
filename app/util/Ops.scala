package com.originate.util

object Ops {
  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
     def ===(other: A): Boolean = self == other
  }
}
