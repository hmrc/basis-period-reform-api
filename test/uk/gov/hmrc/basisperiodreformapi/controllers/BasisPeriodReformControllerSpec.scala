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

import org.mockito.scalatest.ResetMocksAfterEachTest

import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

import uk.gov.hmrc.basisperiodreformapi.mocks._
import uk.gov.hmrc.basisperiodreformapi.models._

class BasisPeriodReformControllerSpec extends HmrcSpec with ResetMocksAfterEachTest with AuthConnectorMockModule with BasisPeriodReformConnectorMockModule {

  private val controller = new BasisPeriodReformController(mockBprConnector, mockAuthConnector, Helpers.stubControllerComponents())

  "GET /views/iv_overlap_relief_partnership" should {
    val fakeRequest = FakeRequest("GET", "/views/iv_overlap_relief_partnership")

    "return 200 when wrapped 200" in {
      Authorise.asPrivilegedApplication()
      GetPartnershipDetails.returns(200, ReliefPartnershipResponse(None, None, None))
      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.OK
      GetPartnershipDetails.verifyCalledWith(None, None)
    }

    "return 400 when wrapped 400" in {
      Authorise.asPrivilegedApplication()
      GetPartnershipDetails.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.BAD_REQUEST
      GetPartnershipDetails.verifyCalledWith(None, None)
    }

    "return 500 when exception" in {
      Authorise.asPrivilegedApplication()
      GetPartnershipDetails.returnsException()
      val result = controller.partnership(None, None)(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      GetPartnershipDetails.verifyCalledWith(None, None)
    }

    "return 401 when standard app" in {
      Authorise.asStandardApplication()
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
      GetSoleTrader.returns(200, SoleTraderResponse(None, None, None))
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.OK
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "return 400 when wrapped 400" in {
      Authorise.asPrivilegedApplication()
      GetSoleTrader.returnsError(400, ApiErrors(List(ApiError("0", "ERROR"))))
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.BAD_REQUEST
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "return 500 when exception" in {
      Authorise.asPrivilegedApplication()
      GetSoleTrader.returnsException()
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      GetSoleTrader.verifyCalledWith(someUtr)
    }

    "return 401 when standard app" in {
      Authorise.asStandardApplication()
      val result = controller.soleTrader(someUtr)(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      GetSoleTrader.verifyNotCalled()
    }
  }
}
