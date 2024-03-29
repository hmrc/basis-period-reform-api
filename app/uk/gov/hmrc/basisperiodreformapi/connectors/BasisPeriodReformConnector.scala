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

package uk.gov.hmrc.basisperiodreformapi.connectors

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

import play.api.http.HeaderNames.AUTHORIZATION
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import uk.gov.hmrc.basisperiodreformapi.config.AppConfig
import uk.gov.hmrc.basisperiodreformapi.models._

@Singleton
class BasisPeriodReformConnector @Inject() (http: HttpClient, appConfig: AppConfig)(implicit ec: ExecutionContext) extends BprMapper {

  lazy val headers: Seq[(String, String)] = Seq(
    AUTHORIZATION -> s"Basic ${appConfig.bprAuthorizationToken}"
  )

  def getPartnershipDetails(utr: Option[String], partnershipNumber: Option[String])(implicit hc: HeaderCarrier): Future[TypedWrappedResponse[ReliefPartnershipResponse]] = {
    http.GET[HttpResponse](
      url = s"${appConfig.bprBaseUrl}/iv_overlap_relief_partnership",
      headers = headers,
      queryParams = Seq(utr.map(("utr", _)), partnershipNumber.map(("partnership_ref_number", _))).collect { case Some(param) => param }
    ).map(enforcePartnership)
  }

  def getSoleTraderDetails(utr: Option[String])(implicit hc: HeaderCarrier): Future[TypedWrappedResponse[SoleTraderResponse]] = {
    http.GET[HttpResponse](
      url = s"${appConfig.bprBaseUrl}/iv_overlap_relief_sole_trader",
      headers = headers,
      queryParams = utr.map(("utr", _)).fold(Seq[(String, String)]())(Seq(_))
    ).map(enforceSoleTrader)
  }
}
