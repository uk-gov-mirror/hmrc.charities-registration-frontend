/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers.common

import config.FrontendAppConfig
import controllers.LocalBaseController
import models.requests.DataRequest
import models.{Mode, PhoneNumber}
import navigation.BaseNavigator
import pages.QuestionPage
import play.api.data.Form
import play.api.mvc._
import service.UserAnswerService
import views.html.common.PhoneNumberView

import scala.concurrent.Future

trait PhoneNumberController extends LocalBaseController {
  protected val sessionRepository: UserAnswerService
  protected val navigator: BaseNavigator
  protected val controllerComponents: MessagesControllerComponents
  protected val view: PhoneNumberView
  protected val messagePrefix: String

  def getView(page: QuestionPage[PhoneNumber], form: Form[PhoneNumber], fullName: String, submitCall: Call)(
    implicit appConfig: FrontendAppConfig, request: DataRequest[AnyContent]): Result = {

      val preparedForm = request.userAnswers.get(page) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, fullName, messagePrefix, submitCall))
  }

  def postView(mode: Mode, page: QuestionPage[PhoneNumber], form: Form[PhoneNumber], fullName: String,
               section: QuestionPage[Boolean], submitCall: Call)(implicit appConfig: FrontendAppConfig, request: DataRequest[AnyContent]): Future[Result] = {

    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, fullName, messagePrefix, submitCall))),

      value =>
        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(page, value).flatMap(_.set(section, false)))
          _              <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(page, mode, updatedAnswers))
    )
  }
}
