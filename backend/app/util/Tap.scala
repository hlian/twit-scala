package com.originate.util

object Tap {
  implicit class WithTap[A](self: A) {
    def tap[B](f: A => B): A = {
      f(self)
      self
    }

    def applyTo[B](f: A => B): B = f(self)
  }
}
