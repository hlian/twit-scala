package com.originate.support

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.BeforeAndAfterAll
import play.api.http.Port
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.libs.ws.ahc.{AhcWSClient, AhcWSClientConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/*
 * WSFunctionalSpec is intended to be used for functional tests that need to make
 * web requests (for example, to hit an external service), and can be mixed in to
 * integration specs to allow for making requests to the server
 */
trait WSFunctionalSpec extends BaseSpec with BeforeAndAfterAll {

  private val port = new Port(-1)
  private val name = "ws-test-client-1"
  private val system = ActorSystem(name)
  private val materializer = ActorMaterializer(namePrefix = Some(name))(system)
  val client = AhcWSClient(AhcWSClientConfig(maxRequestRetry = 0))(materializer)

  override def afterAll(): Unit = {
    client.close()
    system.terminate()
  }

  val wsClient = new WSClient {
    def underlying[T] = client.underlying.asInstanceOf[T] // scalastyle:off token
    def url(url: String) = {
      if (url.startsWith("/") && port.value != -1) {
        client.url(s"http://localhost:$port$url")
      } else {
        client.url(url)
      }
    }
    def close() = ()
  }

  def get(path: String, queryParams: (String, String)*): Future[WSResponse] =
    buildClient(path, queryParams).get

  private def buildClient(path: String, queryParams: Seq[(String, String)]) =
    wsClient.url(path).withQueryString(queryParams: _*)

}
