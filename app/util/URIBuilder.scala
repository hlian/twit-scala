package com.originate.util

import play.api.mvc.RequestHeader

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

object URIBuilder {

  def addQueryToRequestPath(request: RequestHeader, data: Map[String, Seq[String]]): String =
    request.path + "?" + encodeQuery(request.queryString ++ data)

  def encodeQuery(data: Map[String, Seq[String]]): String =
    encodeQueryPieces(data).mkString("&")

  private def encodeQueryPieces(data: Map[String, Seq[String]]): Seq[String] =
    for {
      (key, values) <- data.toSeq if (key.nonEmpty)
      value <- values
      encodedKey = URLEncoder.encode(key, UTF_8.toString)
      encodedValue = URLEncoder.encode(value, UTF_8.toString)
    } yield s"$encodedKey=$encodedValue"

}
