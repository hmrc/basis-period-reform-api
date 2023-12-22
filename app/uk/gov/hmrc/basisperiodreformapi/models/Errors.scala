/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.basisperiodreformapi.models

import play.api.libs.json.{Json, OFormat, Reads}

case class ApiError(code: String, message: String)

object ApiError {
  implicit val format: OFormat[ApiError] = Json.format[ApiError]
}
case class ApiErrors(errors: List[ApiError])

object ApiErrors {
  implicit val format: OFormat[ApiErrors] = Json.format[ApiErrors]
}
case class TypedWrappedResponse[T](status: Int, response: Either[ApiErrors, T])

case class DenodoError(code: Option[Int], message: Option[String])

object DenodoError {
  implicit val errorFormat: Reads[DenodoError] = Json.reads[DenodoError]
}
case class DenodoErrors(errors: List[DenodoError])

object DenodoErrors {
  import play.api.libs.json._

  implicit val reads: Reads[DenodoErrors] = (JsPath \ "__errors__").read[List[DenodoError]].map(DenodoErrors(_))
}
