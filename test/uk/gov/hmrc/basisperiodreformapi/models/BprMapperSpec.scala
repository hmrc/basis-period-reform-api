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

import scala.io.Source

import org.scalatest.prop.TableDrivenPropertyChecks

import play.api.libs.json._
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec
import uk.gov.hmrc.http.HttpResponse

class BprMapperSpec extends HmrcSpec with TableDrivenPropertyChecks {

  "Partnership" should {
    "transform a successful empty partnership" in new BprMapper {
      val cases = Table(("FileName", "status"), ("empty.json", 200), ("single.json", 202), ("multiple.json", 204))

      forAll(cases) {
        case (filename, status) =>
          val body = Json.parse(Source.fromResource(s"./partnership/in/$filename").getLines().mkString)
          val resp = enforcePartnership(HttpResponse(status, body, Map.empty))
          resp.status shouldBe status
          Json.toJson(resp.response.value) shouldBe Json.parse(Source.fromResource(s"./partnership/out/$filename").getLines().mkString)
      }
    }

    "transform an error response" in new BprMapper {
      val body = Json.parse(Source.fromResource(s"./partnership/in/error.json").getLines().mkString)
      val resp = enforcePartnership(HttpResponse(400, body, Map.empty))
      resp.status shouldBe 400
      Json.toJson(resp.response.left.value) shouldBe Json.parse(Source.fromResource(s"./partnership/out/error.json").getLines().mkString)
    }

    "transform an unknown error response" in new BprMapper {
      val resp = enforcePartnership(HttpResponse(400, """{"fish":"cod"}""", Map.empty))
      resp.status shouldBe 400
      Json.toJson(resp.response.left.value) shouldBe Json.parse(Source.fromResource(s"./partnership/out/defaulterror.json").getLines().mkString)
    }

    "transform an empty response" in new BprMapper {
      val resp = enforcePartnership(HttpResponse(400, "", Map.empty))
      resp.status shouldBe 400
      Json.toJson(resp.response.left.value) shouldBe Json.parse(Source.fromResource(s"./partnership/out/defaulterror.json").getLines().mkString)
    }
  }

  "sole trader" should {
    "transform a successful" in new BprMapper {
      val cases = Table(("FileName", "status"), ("empty.json", 200), ("single.json", 202), ("multiple.json", 204))

      forAll(cases) {
        case (filename, status) =>
          val body = Json.parse(Source.fromResource(s"./soletrader/in/$filename").getLines().mkString)
          val resp = enforceSoleTrader(HttpResponse(status, body, Map.empty))
          resp.status shouldBe status
          Json.toJson(resp.response.value) shouldBe Json.parse(Source.fromResource(s"./soletrader/out/$filename").getLines().mkString)
      }
    }

    "transform an error response" in new BprMapper {
      val body = Json.parse(Source.fromResource(s"./soletrader/in/error.json").getLines().mkString)
      val resp = enforceSoleTrader(HttpResponse(400, body, Map.empty))
      resp.status shouldBe 400
      Json.toJson(resp.response.left.value) shouldBe Json.parse(Source.fromResource(s"./soletrader/out/error.json").getLines().mkString)
    }

    "transform an empty response" in new BprMapper {
      val resp = enforceSoleTrader(HttpResponse(400, "", Map.empty))
      resp.status shouldBe 400
      Json.toJson(resp.response.left.value) shouldBe Json.parse(Source.fromResource(s"./soletrader/out/defaulterror.json").getLines().mkString)
    }
  }
}
