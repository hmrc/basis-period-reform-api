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

import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}
import uk.gov.hmrc.http.HttpResponse

trait BprMapper {
  private val defaultError: ApiErrors = ApiErrors(List(ApiError("INTERNAL_SERVER_ERROR", "There was an unknown server error")))

  def enforceResponse[T](response: HttpResponse)(implicit rdr: Reads[T]): TypedWrappedResponse[T] = {
    if (response.status < 400) {
      Json.fromJson[T](response.json) match {
        case JsSuccess(resp, _) => TypedWrappedResponse[T](response.status, Right(resp))
        case JsError(_)         => TypedWrappedResponse[T](INTERNAL_SERVER_ERROR, Left(defaultError))
      }
    } else {
      mapError[T](response)
    }
  }

  def enforcePartnership: HttpResponse => TypedWrappedResponse[ReliefPartnershipResponse] = enforceResponse[ReliefPartnershipResponse]
  def enforceSoleTrader: HttpResponse => TypedWrappedResponse[SoleTraderResponse]         = enforceResponse[SoleTraderResponse]

  private def mapError[T](response: HttpResponse): TypedWrappedResponse[T] = {
    if (response.body.nonEmpty) {
      val err: ApiErrors = Json.fromJson[DenodoErrors](response.json) match {
        case JsSuccess(resp: DenodoErrors, _) => ApiErrors(resp.errors.map((error: DenodoError) =>
            ApiError(s"${error.code.getOrElse(0)}", error.message.getOrElse(""))
          ))
        case JsError(_)                       => defaultError
      }
      TypedWrappedResponse[T](response.status, Left(err))
    } else {
      TypedWrappedResponse[T](response.status, Left(defaultError))
    }
  }
}
