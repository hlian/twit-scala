package com.originate.integration.steps

import com.originate.support.BaseSpecLike

import org.scalatest.concurrent.{AbstractPatienceConfiguration, Eventually}
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

trait StepHelpers extends Eventually
  with AbstractPatienceConfiguration { self: BaseSpecLike with WebBrowser =>

  override implicit val patienceConfig = PatienceConfig(
    timeout = scaled(Span(5, Seconds)), // scalastyle:off magic.number
    interval = scaled(Span(100, Millis)) // scalastyle:off magic.number
  )

  def goHome(): Unit = go to path("/")

  def path(p: String): String =
    s"http://localhost:${appContainer.port}$p"

  def clickButtonOrLink(text: String): Unit =
    Try(click.on(linkText(text))) orElse Try(clickOn(text))

}
