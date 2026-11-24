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

package uk.gov.hmrc.agentriskingfrontend.controllers

import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.agentriskingfrontend.action.agent.AgentActions
import uk.gov.hmrc.agentriskingfrontend.views.html.HelloWorldPage

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartController @Inject() (
  mcc: MessagesControllerComponents,
  actions: AgentActions,
  helloWorldPage: HelloWorldPage
)
extends FrontendControllerBase(mcc):

  // we should be able to use this controller method to work out where a user should be, but for now we will just show the hello world page
  def landing: Action[AnyContent] = Action:
    implicit request =>
      Ok(helloWorldPage())

  def anySignedInPage: Action[AnyContent] = actions.authorised:
    implicit request =>
      Ok(helloWorldPage())
