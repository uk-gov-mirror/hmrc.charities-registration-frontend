/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers.otherOfficials

import config.FrontendAppConfig
import controllers.actions._
import controllers.common.IsAddAnotherController
import forms.common.IsAddAnotherFormProvider
import javax.inject.Inject
import models.{Index, Mode}
import navigation.OtherOfficialsNavigator
import pages.otherOfficials.{AddAnotherOtherOfficialPage, OtherOfficialsNamePage}
import pages.sections.Section8Page
import play.api.data.Form
import play.api.mvc._
import repositories.UserAnswerRepository
import views.html.common.IsAddAnotherView

import scala.concurrent.Future

class AddAnotherOtherOfficialController @Inject()(
    val identify: AuthIdentifierAction,
    val getData: UserDataRetrievalAction,
    val requireData: DataRequiredAction,
    val formProvider: IsAddAnotherFormProvider,
    val controllerComponents: MessagesControllerComponents,
    override val sessionRepository: UserAnswerRepository,
    override val navigator: OtherOfficialsNavigator,
    override val view: IsAddAnotherView
  )(implicit appConfig: FrontendAppConfig) extends IsAddAnotherController {

  val messagePrefix: String = "addAnotherOtherOfficial"
  val form: Form[Boolean] = formProvider(messagePrefix)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getFullName(OtherOfficialsNamePage(Index(0))) { firstOfficialsName =>
        getFullName(OtherOfficialsNamePage(Index(1))) { secondOfficialsName =>
          Future.successful(getView(AddAnotherOtherOfficialPage, form, firstOfficialsName, Some(secondOfficialsName),
            controllers.otherOfficials.routes.AddAnotherOtherOfficialController.onSubmit(mode)))
        }
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getFullName(OtherOfficialsNamePage(0)) { firstOfficialsName =>
        getFullName(OtherOfficialsNamePage(1)) { secondOfficialsName =>
          postView(mode, AddAnotherOtherOfficialPage, form, firstOfficialsName, Some(secondOfficialsName), Section8Page,
            controllers.otherOfficials.routes.AddAnotherOtherOfficialController.onSubmit(mode))
        }
      }
  }
}
