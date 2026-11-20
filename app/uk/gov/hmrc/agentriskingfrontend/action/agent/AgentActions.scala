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

package uk.gov.hmrc.agentriskingfrontend.action.agent

import play.api.mvc.*
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuilders.refineFutureEither
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuilders.refineUnion
import uk.gov.hmrc.agentriskingfrontend.action.ActionBuildersWithData
import uk.gov.hmrc.agentriskingfrontend.action.RequestWithDataCt
import uk.gov.hmrc.agentriskingfrontend.controllers.AppRoutes
import uk.gov.hmrc.agentriskingfrontend.util.RequestAwareLogging
import uk.gov.hmrc.auth.core.retrieve.Credentials

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.ExecutionContext

object AgentActions:

  export uk.gov.hmrc.agentriskingfrontend.action.Actions.*

  type DataWithAuth = (InternalUserId, GroupId, Credentials)

@Singleton
class AgentActions @Inject()(
                                   defaultActionBuilder: DefaultActionBuilder,
                                   authorisedActionRefiner: AgentAuthRefiner
)(using ExecutionContext)
extends RequestAwareLogging:

  export ActionBuildersWithData.*
  export AgentActions.*

  val action: ActionBuilderWithData[EmptyTuple] = defaultActionBuilder
    .refineUnion(request => RequestWithDataCt.empty(request))

  val authorised: ActionBuilderWithData[DataWithAuth] = action
    .refineFutureEither(authorisedActionRefiner.refine)
