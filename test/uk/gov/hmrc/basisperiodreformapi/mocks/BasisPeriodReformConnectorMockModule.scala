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

package uk.gov.hmrc.basisperiodreformapi.mocks

import scala.concurrent.Future

import org.mockito.{ArgumentMatchersSugar, MockitoSugar}

import uk.gov.hmrc.http.HttpException

import uk.gov.hmrc.basisperiodreformapi.connectors._
import uk.gov.hmrc.basisperiodreformapi.models.{ApiErrors, ReliefPartnershipResponse, TypedWrappedResponse}

trait BasisPeriodReformConnectorMockModule extends MockitoSugar with ArgumentMatchersSugar {
  val mockBprConnector: BasisPeriodReformConnector = mock[BasisPeriodReformConnector]

  object GetPartnershipDetails {

    def returns(status: Int, response: ReliefPartnershipResponse): Unit = {
      when(mockBprConnector.getPartnershipDetails(*, *)(*)).thenReturn(Future.successful(TypedWrappedResponse(status, Right(response))))
    }

    def returnsError(status: Int, response: ApiErrors): Unit = {
      when(mockBprConnector.getPartnershipDetails(*, *)(*)).thenReturn(Future.successful(TypedWrappedResponse(status, Left(response))))
    }

    def returnsException(): Unit = {
      when(mockBprConnector.getPartnershipDetails(*, *)(*)).thenReturn(Future.failed(new HttpException("error", 500)))
    }

    def verifyCalledWith(utr: Option[String], partnershipRef: Option[String]): Unit = {
      verify(mockBprConnector).getPartnershipDetails(eqTo(utr), eqTo(partnershipRef))(*)
    }

    def verifyNotCalled(): Unit = {
      verify(mockBprConnector, never).getPartnershipDetails(*, *)(*)
    }
  }

}
