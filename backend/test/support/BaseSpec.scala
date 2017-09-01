package com.originate.support

import com.originate.MockRegistry
import com.originate.util.Tap._

import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Try

trait BaseSpecLike {

  trait BaseContext extends MockRegistry with MockitoSugar

  val defaultTimeout =
    if (Option(System.getenv("CIRCLECI")).isDefined) 1.minute
    else 1.second

  def fs[T](t: T): Future[T] = Future.successful(t)

  def await[T](future: Future[T], timeout: Duration = defaultTimeout): T = Await.result(future, timeout)

  def ready[T](future: Future[T], timeout: Duration = defaultTimeout): Future[T] =
    Await.ready(future, timeout)

  def whenReady[T](future: Future[T], timeout: Duration = defaultTimeout)(body: T => Unit): Unit =
    await(future, timeout) tap body

  def afterComplete[T](future: Future[T], timeout: Duration = defaultTimeout)(body: => Unit): Unit =
    await(future, timeout) tap (_ => body)

  def whenComplete[T](future: Future[T], timeout: Duration = defaultTimeout)(body: Try[T] => Unit): Unit =
    Try(await(future, timeout)) tap body

}

abstract class BaseSpec extends FreeSpec with BaseSpecLike with Matchers with MockHelpers
