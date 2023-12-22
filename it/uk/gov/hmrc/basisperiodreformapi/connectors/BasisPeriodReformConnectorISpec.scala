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

import uk.gov.hmrc.basisperiodreformapi.AsyncHmrcSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, Configuration}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import uk.gov.hmrc.basisperiodreformapi.connectors.stubs.BprStub
import uk.gov.hmrc.basisperiodreformapi.models.{ApiErrors, DenodoErrors, ReliefPartnershipResponse, TypedWrappedResponse}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.test.WireMockSupport

import scala.io.Source

class BasisPeriodReformConnectorISpec extends AsyncHmrcSpec with GuiceOneAppPerSuite with WireMockSupport {

  val stubConfig: Configuration  = Configuration(
    "microservice.services.bpr.port"   -> wireMockPort,
    "microservice.services.bpr.prefix" -> "",
    "metrics.enabled"                  -> false,
    "auditing.enabled"                 -> false
  )
  implicit val hc: HeaderCarrier = HeaderCarrier()

  override def fakeApplication(): Application = GuiceApplicationBuilder()
    .configure(stubConfig)
    .build()
  val underTest                               = app.injector.instanceOf[BasisPeriodReformConnector]

  "partnership details" should {
    "return success" in {
      BprStub.stubGetPartnership(200, Source.fromResource("partnership/in/single.json").getLines().mkString)
      val expectedResponse = Json.parse(Source.fromResource("partnership/out/single.json").getLines().mkString).as[ReliefPartnershipResponse]

      val result = await(underTest.getPartnershipDetails(Some("123"), Some("456")))
      result shouldBe TypedWrappedResponse(200, Right(expectedResponse))
    }

    "return error" in {
      BprStub.stubGetPartnership(400, Source.fromResource("partnership/in/error.json").getLines().mkString)
      val expectedResponse = Json.parse(Source.fromResource("partnership/out/error.json").getLines().mkString).as[ApiErrors]

      val result = await(underTest.getPartnershipDetails(Some("123"), Some("456")))
      result shouldBe TypedWrappedResponse(400, Left(expectedResponse))
    }
  }
}
