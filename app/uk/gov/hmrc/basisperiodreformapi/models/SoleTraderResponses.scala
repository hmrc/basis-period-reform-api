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

import play.api.libs.json._

case class SoleTrader(
    sequenceNumber: Option[Double],
    tieBreaker: Option[Double],
    name: Option[String],
    utr: Option[Long],
    nino: Option[String],
    sourceNumber: Option[Double],
    nameOfBusiness: Option[String],
    descriptionOfBusiness: Option[String],
    dateOfCommencement: Option[String],
    dateCeased: Option[String],
    accountingPeriodStart: Option[String],
    accountingPeriodEnd: Option[String],
    basisPeriodStart: Option[String],
    basisPeriodEnd: Option[String],
    accountingDateChanged: Option[String],
    overlapReliefUsedThisYear: Option[Double],
    overlapProfitCarriedForward: Option[Double],
    netBusinessProfit: Option[Double],
    netBusinessLoss: Option[Double]
  )

object SoleTrader {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val soleTraderFormat: Format[SoleTrader] = (
    (JsPath \ "seq_no").formatNullable[Double] and
      (JsPath \ "tie_breaker").formatNullable[Double] and
      (JsPath \ "name").formatNullable[String] and
      (JsPath \ "utr").formatNullable[Long] and
      (JsPath \ "nino").formatNullable[String] and
      (JsPath \ "source_number").formatNullable[Double] and
      (JsPath \ "name_of_business").formatNullable[String] and
      (JsPath \ "description_of_business").formatNullable[String] and
      (JsPath \ "date_of_commencement").formatNullable[String] and
      (JsPath \ "date_ceased").formatNullable[String] and
      (JsPath \ "accounting_period_starts").formatNullable[String] and
      (JsPath \ "accounting_period_ends").formatNullable[String] and
      (JsPath \ "basis_period_starts").formatNullable[String] and
      (JsPath \ "basis_period_ends").formatNullable[String] and
      (JsPath \ "accounting_date_changed").formatNullable[String] and
      (JsPath \ "overlap_relief_used_this_year").formatNullable[Double] and
      (JsPath \ "overlap_profit_carried_forward").formatNullable[Double] and
      (JsPath \ "net_business_profit").formatNullable[Double] and
      (JsPath \ "net_business_loss").formatNullable[Double]
  )(SoleTrader.apply, unlift(SoleTrader.unapply))

}
case class SoleTraderAuditDetails(utr: Option[String], reliefPartnerships: Option[List[SoleTrader]])

object SoleTraderAuditDetails {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  implicit val auditFormat: OFormat[SoleTrader] = Json.format[SoleTrader]

  implicit val format: OFormat[SoleTraderAuditDetails] = ((JsPath \ "utr").formatNullable[String] and
    (JsPath \ "reliefPartnerships").formatNullable[List[SoleTrader]])(
    SoleTraderAuditDetails.apply,
    unlift(SoleTraderAuditDetails.unapply)
  )
}
case class SoleTraderResponse(name: Option[String], description: Option[String], elements: Option[List[SoleTrader]])

object SoleTraderResponse {
  implicit val format: OFormat[SoleTraderResponse] = Json.format[SoleTraderResponse]
}
