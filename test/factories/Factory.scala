package com.originate.factories

import com.originate.models.WithId
import com.originate.util.Tap._

import java.util.concurrent.atomic.AtomicInteger

trait FactoryLike[T] {
  private val idCounter = new AtomicInteger(1)

  def nextId: Int = idCounter.get tap (i => idCounter.set(i + 1))

  def newInstance: T

  def newInstanceSeq(count: Int): Seq[T] = Seq.fill(count)(newInstance)
}

trait ImmutableFactory[T] extends FactoryLike[T] {
  def createInstance: WithId[T] = WithId(nextId, newInstance)

  def createInstanceSeq(count: Int): Seq[WithId[T]] = Seq.fill(count)(createInstance)
}

trait Factory[T] extends ImmutableFactory[T] {
  def alter(t: T): T

  def alter(withId: WithId[T]): WithId[T] = withId.copy(model = alter(withId.model))
}
