package com.originate.integration.steps

import com.originate.support.BaseSpecLike

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import org.scalatest.selenium.WebBrowser

trait BaseSteps
  extends ScalaDsl
  with EN
  with Matchers
  with WebBrowser
  with BaseSpecLike
  with StepHelpers
