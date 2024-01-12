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

package uk.gov.hmrc.basisperiodreformapi.controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

import org.mockito.scalatest.ResetMocksAfterEachTest

import play.api.http.Status
import play.api.libs.json.{JsString, _}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

import uk.gov.hmrc.basisperiodreformapi.mocks._
import uk.gov.hmrc.basisperiodreformapi.models._

class BasisPeriodReformControllerSpec extends HmrcSpec with ResetMocksAfterEachTest with AuthConnectorMockModule with BasisPeriodReformConnectorMockModule
    with AuditServiceMockModule {

  private val controller = new BasisPeriodReformController(mockBprConnector, mockAuditService, mockAuthConnector, Helpers.stubControllerComponents())

  "GET /views/iv_overlap_relief_partnership" should {
    val fakeRequest = FakeRequest("GET", "/views/iv_overlap_relief_partnership")

    "return 200 when wrapped 200" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returns(200, ReliefPartnershipResponse(None, None, None))
      val result       = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.OK
      GetPartnershipDetails.verifyCalledWith(None, None)
      val auditPayload = Audit.verifyCalledWith()
      auditPayload.toString() shouldBe "{}"
    }

    "should audit empty object when returned empty" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returns(200, ReliefPartnershipResponse(None, None, None))
      await(controller.partnership(None, None)(fakeRequest))

      val auditPayload = Audit.verifyCalledWith()
      auditPayload.toString() shouldBe "{}"
    }

    "should audit actual object" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returns(200, Json.parse(Source.fromResource("partnership/in/single.json").getLines().mkString).as[ReliefPartnershipResponse])
      await(controller.partnership(Some("987654321"), Some("5600000015"))(fakeRequest))
      val auditPayload = Audit.verifyCalledWith()
      auditPayload shouldBe Json.parse(Source.fromResource("partnership/audit/single.json").getLines().mkString)
    }

    "should audit empty object when returned error" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      await(controller.partnership(None, None)(fakeRequest))

      val auditPayload = Audit.verifyCalledWith()
      auditPayload.toString() shouldBe "{}"
    }

    "return 400 when wrapped 400" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.BAD_REQUEST
      GetPartnershipDetails.verifyCalledWith(None, None)
    }

    "return 500 when exception" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetPartnershipDetails.returnsException()
      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      GetPartnershipDetails.verifyCalledWith(None, None)
    }

    "return 401 when standard app" in {
      Authorise.asStandardApplication()
      Audit.succeeds()

      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      GetPartnershipDetails.verifyNotCalled()
    }
  }

  "GET /views/iv_overlap_relief_sole_trader" should {

    val fakeRequest = FakeRequest("GET", "/views/iv_overlap_relief_sole_trader")
    val someUtr     = Some("987654321")

    "return 200 when wrapped 200" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetSoleTrader.returns(200, SoleTraderResponse(None, None, None))
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.OK
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "should audit empty object when returned empty" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetSoleTrader.returns(200, SoleTraderResponse(None, None, None))
      await(controller.soleTrader(someUtr)(fakeRequest))

      val auditPayload = Audit.verifyCalledWith()
      (auditPayload \ "utr").get shouldBe JsString(someUtr.get)
    }

    "should audit actual object" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetSoleTrader.returns(200, Json.parse(Source.fromResource("soletrader/in/single.json").getLines().mkString).as[SoleTraderResponse])
      await(controller.soleTrader(someUtr)(fakeRequest))
      val auditPayload = Audit.verifyCalledWith()
      auditPayload shouldBe Json.parse(Source.fromResource("soletrader/audit/single.json").getLines().mkString)
    }

    "should audit empty object when returned error" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetSoleTrader.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      await(controller.soleTrader(someUtr)(fakeRequest))

      val auditPayload = Audit.verifyCalledWith()
      auditPayload.toString() shouldBe "{}"
    }

    "return 400 when wrapped 400" in {
      Authorise.asPrivilegedApplication()

      Audit.succeeds()

      GetSoleTrader.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.BAD_REQUEST
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "return 500 when exception" in {
      Authorise.asPrivilegedApplication()
      Audit.succeeds()

      GetSoleTrader.returnsException()
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "return 401 when standard app" in {
      Authorise.asStandardApplication()
      Audit.succeeds()

      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      GetSoleTrader.verifyNotCalled()
    }
  }
}
