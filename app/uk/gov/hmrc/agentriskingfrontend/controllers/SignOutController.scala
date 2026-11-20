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
import play.api.mvc.DefaultActionBuilder
import play.api.mvc.MessagesControllerComponents
import sttp.model.Uri.UriContext
import uk.gov.hmrc.agentriskingfrontend.config.AppConfig
import uk.gov.hmrc.agentriskingfrontend.util.RequestSupport.validateRedirectUrl
import uk.gov.hmrc.agentriskingfrontend.views.html.SignedOutPage
import uk.gov.hmrc.agentriskingfrontend.views.html.TimedOutPage
import uk.gov.hmrc.play.bootstrap.binders.RedirectUrl

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignOutController @Inject() (
  mcc: MessagesControllerComponents,
  defaultActionBuilder: DefaultActionBuilder,
  signedOutPage: SignedOutPage,
  timedOutPage: TimedOutPage,
  appConfig: AppConfig
)
extends FrontendControllerBase(mcc):

  def signOutWithContinue(continue: RedirectUrl): Action[AnyContent] = defaultActionBuilder:
    val validatedRedirect = validateRedirectUrl(continue, appConfig.allowedRedirectHosts)
    val signOutAndRedirectUrl: String = uri"""${appConfig.basFrontendSignOutUrlBase}?${Map("continue" -> validatedRedirect)}""".toString
    Redirect(signOutAndRedirectUrl)

  def signOut(signBackInUrl: Option[RedirectUrl]): Action[AnyContent] = defaultActionBuilder:
    implicit request =>
      val signBackIn = signBackInUrl.getOrElse(RedirectUrl(appConfig.thisFrontendBaseUrl + AppRoutes.StartController.landing.url))
      val continueUrl = uri"${appConfig.thisFrontendBaseUrl + AppRoutes.SignOutController.signedOut(signBackIn).url}"
      Redirect(AppRoutes.SignOutController.signOutWithContinue(RedirectUrl(continueUrl.toString)))

  def signedOut(signBackInUrl: RedirectUrl): Action[AnyContent] = defaultActionBuilder:
    implicit request =>
      Ok(signedOutPage(signBackInUrl))

  def timeOut(signBackInUrl: RedirectUrl): Action[AnyContent] = defaultActionBuilder:
    val continueUrl = uri"${appConfig.thisFrontendBaseUrl + AppRoutes.SignOutController.timedOut(signBackInUrl).url}"
    Redirect(AppRoutes.SignOutController.signOutWithContinue(RedirectUrl(continueUrl.toString)))

  def timedOut(signBackInUrl: RedirectUrl): Action[AnyContent] = defaultActionBuilder:
    implicit request =>
      Ok(timedOutPage(signBackInUrl))
