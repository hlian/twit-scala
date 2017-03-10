package com.originate.util

import play.api.libs.json.{JsArray, JsObject, JsValue}

@SuppressWarnings(Array("org.wartremover.warts.Option2Iterable"))
object JsonTransformer {

  /**
   * Scans a JsValue for JsObjects matching a specific condition and removes them. If the top level
   * JsValue is a JsObject itself, and it matches, then this function returns None.
   */
  def pruneObject(value: JsValue)(f: JsObject => Boolean): Option[JsValue] =
    value match {
      case json: JsObject if f(json) => None
      case json: JsObject => pruneObjectFields(json)(f)
      case array: JsArray => Some(pruneArray(array)(f))
      case _ => Some(value)
    }

  private def pruneObjectFields(json: JsObject)(f: JsObject => Boolean): Option[JsObject] = {
    val updated = json.fields flatMap {
      case (key, jsObject: JsObject) if f(jsObject) => None
      case (key, jsObject: JsObject) => pruneObject(jsObject)(f) map (key -> _)
      case (key, jsArray: JsArray) => Some(key -> pruneArray(jsArray)(f))
      case entry: Any => Some(entry)
    }

    Some(JsObject(updated))
  }

  private def pruneArray(json: JsArray)(f: JsObject => Boolean): JsArray =
    JsArray(json.value flatMap (pruneObject(_)(f)))

}
