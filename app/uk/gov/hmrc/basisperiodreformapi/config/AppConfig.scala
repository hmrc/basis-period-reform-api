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

package uk.gov.hmrc.basisperiodreformapi.config

import javax.inject.{Inject, Singleton}

import play.api.Configuration

@Singleton
class AppConfig @Inject() (config: Configuration) {

  val appName: String   = config.get[String]("appName")
  val apiStatus: String = config.get[String]("apiStatus")

  private val bprProtocol: String  = config.get[String]("microservice.services.bpr.protocol")
  private val bprHost: String      = config.get[String]("microservice.services.bpr.host")
  private val bprPort: String      = config.get[String]("microservice.services.bpr.port")
  private val bprUriPrefix: String = config.getOptional[String]("microservice.services.bpr.prefix").getOrElse("")

  val bprAuthorizationToken: String = config.get[String]("microservice.services.bpr.token")
  val bprBaseUrl: String            = s"$bprProtocol://$bprHost:$bprPort$bprUriPrefix"
}
