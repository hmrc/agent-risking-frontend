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

package uk.gov.hmrc.agentriskingfrontend.views

import play.api.i18n.I18nSupport
import play.api.i18n.Messages
import play.api.i18n.MessagesApi
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results.Unauthorized
import play.api.mvc.Results.NotFound
import play.api.mvc.Results.Forbidden
import uk.gov.hmrc.agentriskingfrontend.views.html.ErrorTemplate

import javax.inject.Inject

class ErrorResults @Inject()(
  errorTemplate: ErrorTemplate,
  override val messagesApi: MessagesApi
)
extends I18nSupport:

  def unauthorised(message: String = "")(using request: RequestHeader): Result = Unauthorized(
    errorTemplate(
      heading = Messages("unauthorised.heading"),
      message = message
    )
  )
  
  def notFound(message: Option[String] = None)(using request: RequestHeader): Result = NotFound(
    errorTemplate(
      heading = Messages("notFound.heading"),
      message = message.getOrElse(Messages("notFound.message"))
    )
  )
  
  def wrongCredentialRole(message: Option[String] = None)(using request: RequestHeader): Result = Forbidden(
    errorTemplate(
      heading = Messages("wrongCredentialRole.heading"),
      message = message.getOrElse(Messages("wrongCredentialRole.message"))
    )
  )
  
  def missingEnrolment(message: Option[String] = None)(using request: RequestHeader): Result = Forbidden(
    errorTemplate(
      heading = Messages("missingEnrolment.heading"),
      message = message.getOrElse(Messages("missingEnrolment.message"))
    )
  )
  
  
