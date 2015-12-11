package org.qza.bs.health

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class HealthResponse(status: String)

trait HealthResponseJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val healthResponseJson = jsonFormat1(HealthResponse)
}