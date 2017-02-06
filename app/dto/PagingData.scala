package com.originate.dto

import com.originate.util.URIBuilder

import play.api.mvc.QueryStringBindable

import scala.util.Right

/**
 * Used to package up and pass along paging data from the frontend to
 * the rest of the system
 */
case class PagingData private (limit: Int, offset: Int) {
  def previous: PagingData =
    copy(
      offset = Seq(offset - limit, 0) reduceLeftOption (_ max _) getOrElse 0
    )

  def next: PagingData =
    copy(offset = offset + limit)

  def toQueryKeys: Map[String, Seq[String]] =
    Map(
      "limit" -> List(limit.toString),
      "offset" -> List(offset.toString)
    )
}

object PagingData {

  val MaxLimit = 100
  val DefaultLimit = 20

  def default: PagingData = PagingData(MaxLimit, 0)

  def apply(
      limit: Int,
      offset: Option[Int] = None): PagingData = {
    val fixedLimit =
      if (limit > MaxLimit) MaxLimit
      else if (limit <= 0) DefaultLimit
      else limit

    PagingData(fixedLimit, offset.getOrElse(0))
  }

  implicit val queryStringBinder: QueryStringBindable[PagingData] = new QueryStringBindable[PagingData] {
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, PagingData]] = {
      val limit = bindInt("limit", params) getOrElse DefaultLimit
      val offset = bindInt("offset", params)

      Some(Right(PagingData(limit, offset)))
    }

    def unbind(key: String, pagingData: PagingData): String =
      URIBuilder.encodeQuery(pagingData.toQueryKeys)

    private def bindInt(key: String, params: Map[String, Seq[String]]): Option[Int] =
      QueryStringBindable.bindableInt.bind(key, params) flatMap (_.right.toOption)
  }

}
