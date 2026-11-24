/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.agentriskingfrontend.shared

import org.bson.types.ObjectId
import play.api.libs.json.Format
import play.api.mvc.PathBindable
import uk.gov.hmrc.agentriskingfrontend.shared.util.JsonFormatsFactory
import uk.gov.hmrc.agentriskingfrontend.shared.util.ValueClassBinder

import javax.inject.Singleton

/** Risking Case Identifier, which is unique for a risking case
  */
final case class RiskingCaseId(value: String)

object RiskingCaseId:

  given format: Format[RiskingCaseId] = JsonFormatsFactory.makeValueClassFormat
  given pathBindable: PathBindable[RiskingCaseId] = ValueClassBinder.valueClassBinder[RiskingCaseId](_.value)

@Singleton
class RiskingCaseIdGenerator:
  def nextRiskingCaseId(): RiskingCaseId = RiskingCaseId(ObjectId.get().toHexString)
