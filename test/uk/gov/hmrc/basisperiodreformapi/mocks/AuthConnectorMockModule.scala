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

import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import uk.gov.hmrc.auth.core.AuthProvider.PrivilegedApplication
import uk.gov.hmrc.auth.core.retrieve.EmptyRetrieval
import uk.gov.hmrc.auth.core.{AuthProviders, UnsupportedAuthProvider}

import uk.gov.hmrc.basisperiodreformapi.connectors.AuthConnector

trait AuthConnectorMockModule extends MockitoSugar {

  val mockAuthConnector = mock[AuthConnector]

  object Authorise {

    def asPrivilegedApplication() = {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(PrivilegedApplication)), eqTo(EmptyRetrieval))(any(), any()))
        .thenReturn(Future.successful(None))
    }

    def asStandardApplication() = {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(PrivilegedApplication)), eqTo(EmptyRetrieval))(any(), any()))
        .thenReturn(Future.failed(UnsupportedAuthProvider()))
    }
  }
}
