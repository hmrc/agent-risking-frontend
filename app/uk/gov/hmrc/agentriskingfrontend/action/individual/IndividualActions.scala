/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.agentriskingfrontend.action.individual

import play.api.mvc.*
import uk.gov.hmrc.agentriskingfrontend.util.shared.SafeEquals.===
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuilders.refineFutureEither
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuilders.refineUnion
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuildersWithData
import uk.gov.hmrc.agentriskingfrontend.action.RequestWithDataCt
import uk.gov.hmrc.agentriskingfrontend.controllers.AppRoutes
import uk.gov.hmrc.agentriskingfrontend.util.RequestAwareLogging
import uk.gov.hmrc.auth.core.ConfidenceLevel
import uk.gov.hmrc.auth.core.retrieve.Credentials

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

object IndividualActions:

  export uk.gov.hmrc.agentriskingfrontend.action.Actions.*
  
  type DataWithAuth = (InternalUserId, Credentials)
  type DataWithAuthAndCl = ConfidenceLevel *: DataWithAuth
  type DataWithTaxIds = Option[Nino] *: Option[SaUtr] *: DataWithAuthAndCl

@Singleton
class IndividualActions @Inject(
) (
  defaultActionBuilder: DefaultActionBuilder,
  individualAuthorisedRefiner: IndividualAuthRefiner
)(using
  ExecutionContext
)
extends RequestAwareLogging:

  export ActionBuildersWithData.*
  export IndividualActions.*

  val action: ActionBuilderWithData[EmptyTuple] = defaultActionBuilder
    .refineUnion(request => RequestWithDataCt.empty(request))

  val authorised: ActionBuilderWithData[DataWithAuthAndCl] = action
    .refineFutureEither(individualAuthorisedRefiner.refineIntoRequestWithAuth)

  val authorisedWithAdditionalIdentifiers: ActionBuilderWithData[DataWithTaxIds] = action
    .refineFutureEither(individualAuthorisedRefiner.refineIntoRequestWithAdditionalIdentifiers)

  private def matchSafeNino(
    ninoFromAuth: Option[Nino],
    confidenceLevel: ConfidenceLevel,
    individualNino: Option[IndividualNino]
  ): Boolean = {
    (confidenceLevel, ninoFromAuth, individualNino) match
      case (cl, Some(nino), Some(Provided(individualNino))) if cl >= ConfidenceLevel.L250 => nino.value.take(8) === individualNino.value.take(8) // compare without suffix letter, as auth nino may not have suffix letter
      case (cl, Some(nino), Some(FromAuth(individualNino))) if cl >= ConfidenceLevel.L250 => nino.value.take(8) === individualNino.value.take(8) // compare without suffix letter, as auth nino may not have suffix letter
      case _ => false
  }
