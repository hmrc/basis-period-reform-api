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

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.auth.core.AuthProvider.PrivilegedApplication
import uk.gov.hmrc.auth.core.{AuthProviders, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import uk.gov.hmrc.basisperiodreformapi.connectors.{AuthConnector, BasisPeriodReformConnector}
import uk.gov.hmrc.basisperiodreformapi.models._
import uk.gov.hmrc.basisperiodreformapi.services.AuditService

@Singleton()
class BasisPeriodReformController @Inject() (
    bprConnector: BasisPeriodReformConnector,
    auditService: AuditService,
    val authConnector: AuthConnector,
    cc: ControllerComponents
  )(implicit ec: ExecutionContext
  ) extends BackendController(cc) with AuthorisedFunctions {

  def partnership(utr: Option[String], partnershipNumber: Option[String]): Action[AnyContent] =
    Action.async { implicit request =>
      authorised(AuthProviders(PrivilegedApplication)) {
        for {
          wrapped <- bprConnector.getPartnershipDetails(utr, partnershipNumber)
          _        = auditService.audit("GetOverlapReliefPartnership", toAuditDetails(utr, partnershipNumber, wrapped))
        } yield Status(wrapped.status)(wrapped.response.fold(Json.toJson(_), Json.toJson(_)))
      } recover recovery
    }

  private def toAuditDetails(utr: Option[String], partnershipNumber: Option[String], wrapped: TypedWrappedResponse[ReliefPartnershipResponse]): JsObject = wrapped.response match {
    case Right(details) => Json.toJsObject(PartnershipAuditDetails(utr, partnershipNumber, details.elements))
    case Left(_)        => Json.obj()
  }

  def soleTrader(utr: Option[String]): Action[AnyContent] =
    Action.async { implicit request =>
      authorised(AuthProviders(PrivilegedApplication)) {
        for {
          wrapped <- bprConnector.getSoleTraderDetails(utr)
          _        = auditService.audit("GetOverlapReliefSoleTrader", toAuditDetails(utr, wrapped))

        } yield Status(wrapped.status)(wrapped.response.fold(Json.toJson(_), Json.toJson(_)))
      } recover recovery
    }

  private def toAuditDetails(utr: Option[String], wrapped: TypedWrappedResponse[SoleTraderResponse]): JsObject = wrapped.response match {
    case Right(details) => Json.toJsObject(SoleTraderAuditDetails(utr, details.elements))
    case Left(_)        => Json.obj()
  }
}
