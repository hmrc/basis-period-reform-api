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

import play.api.libs.json.{Json, _}

case class ReliefPartnership(
    sequenceNumber: Option[Double],
    tieBreaker: Option[Double],
    name: Option[String],
    utr: Option[Long],
    nino: Option[String],
    partnershipRefNumber: Option[Long],
    dateYouBecamePartner: Option[String],
    dateYouLeftPartnership: Option[String],
    basisPeriodStart: Option[String],
    basisPeriodEnd: Option[String],
    shareOfProfitOrLoss: Option[Double],
    netBusinessLossPartners: Option[Double],
    overlapReliefUsedThisYear: Option[Double],
    overlapProfitCarriedForward: Option[Double]
  )

object ReliefPartnership {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val reliefFormat: Format[ReliefPartnership] = (
    (JsPath \ "seq_no").formatNullable[Double] and
      (JsPath \ "tie_breaker").formatNullable[Double] and
      (JsPath \ "name").formatNullable[String] and
      (JsPath \ "utr").formatNullable[Long] and
      (JsPath \ "nino").formatNullable[String] and
      (JsPath \ "partnership_ref_number").formatNullable[Long] and
      (JsPath \ "date_you_became_a_partner").formatNullable[String] and
      (JsPath \ "date_you_left_the_partnership").formatNullable[String] and
      (JsPath \ "basis_period_starts").formatNullable[String] and
      (JsPath \ "basis_period_ends").formatNullable[String] and
      (JsPath \ "share_of_profit_or_loss").formatNullable[Double] and
      (JsPath \ "net_business_loss_partners").formatNullable[Double] and
      (JsPath \ "overlap_relief_used_this_year").formatNullable[Double] and
      (JsPath \ "overlap_profit_carried_forward").formatNullable[Double]
  )(ReliefPartnership.apply, unlift(ReliefPartnership.unapply))

}

case class ReliefPartnershipResponse(name: Option[String], description: Option[String], elements: Option[List[ReliefPartnership]])

object ReliefPartnershipResponse {
  implicit val format: OFormat[ReliefPartnershipResponse] = Json.format[ReliefPartnershipResponse]
}
