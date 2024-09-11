/*
 * Copyright 2024 HM Revenue & Customs
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

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import play.api.libs.json.JsObject

import uk.gov.hmrc.basisperiodreformapi.services.AuditService

trait AuditServiceMockModule extends MockitoSugar {

  val mockAuditService = mock[AuditService]

  object Audit {

    def succeeds() = {
      doNothing.when(mockAuditService).audit(any(), any())(any())
    }

    def verifyCalledWith() = {
      val capture = ArgumentCaptor.forClass(classOf[JsObject])
      verify(mockAuditService).audit(any(), capture.capture())(any())
      capture.getValue
    }
  }
}
