package com.originate.integration

import com.originate.support.IntegrationSpecLike
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import scala.collection.mutable
import scala.util.Random
import scala.concurrent.ExecutionContext

class CucumberApplicationLoader extends IntegrationSpecLike

package object steps {

  val appContainer = new CucumberApplicationLoader
  val app = appContainer.app
  val registry = appContainer.registry

  implicit val connection = registry.connection
  implicit val webDriver: WebDriver = new ChromeDriver
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

}
