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

package controllers

import base.SpecBase
import controllers.actions.{AuthIdentifierAction, FakeAuthIdentifierAction}
import forms.common.YesNoFormProvider
import models.{Index, Name, NormalMode, SelectTitle, UserAnswers}
import navigation.AuthorisedOfficialsNavigator
import navigation.FakeNavigators.FakeAuthorisedOfficialsNavigator
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, _}
import org.scalatest.BeforeAndAfterEach
import pages.EmailOrPostPage
import pages.authorisedOfficials.{AuthorisedOfficialsNamePage, IsAuthorisedOfficialNinoPage}
import play.api.data.Form
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import repositories.UserAnswerRepository
import views.html.EmailOrPostView
import views.html.common.YesNoView

import scala.concurrent.Future

class EmailOrPostControllerSpec extends SpecBase with BeforeAndAfterEach {

  override lazy val userAnswers: Option[UserAnswers] = Some(emptyUserAnswers)

  override def applicationBuilder(): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[UserAnswerRepository].toInstance(mockUserAnswerRepository),
        bind[AuthorisedOfficialsNavigator].toInstance(FakeAuthorisedOfficialsNavigator),
        bind[AuthIdentifierAction].to[FakeAuthIdentifierAction]
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockUserAnswerRepository)
  }

  private val messageKeyPrefix: String = "emailOrPost"
  private val view: EmailOrPostView = injector.instanceOf[EmailOrPostView]
  private val formProvider: YesNoFormProvider = injector.instanceOf[YesNoFormProvider]
  private val form: Form[Boolean] = formProvider(messageKeyPrefix)

  private val controller: EmailOrPostController = inject[EmailOrPostController]

  "EmailOrPost Controller" must {

    "return OK and the correct view for a GET" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers)))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, Seq("requiredDocuments.governingDocumentName.answerTrue"), None
      )(fakeRequest, messages, frontendAppConfig).toString
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers.
        set(EmailOrPostPage, true).success.value)))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustEqual OK
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

    "redirect to Registration Sent page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers)))
      when(mockUserAnswerRepository.set(any())).thenReturn(Future.successful(true))

      val result = controller.onSubmit()(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.RegistrationSentController.onPageLoad().url)
      verify(mockUserAnswerRepository, times(1)).get(any())
      verify(mockUserAnswerRepository, times(1)).set(any())
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody()

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers)))

      val result = controller.onSubmit()(request)

      status(result) mustBe BAD_REQUEST
      verify(mockUserAnswerRepository, times(1)).get(any())
      verify(mockUserAnswerRepository, never).set(any())
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(None))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.SessionExpiredController.onPageLoad().url)
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(None))

      val result = controller.onSubmit()(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(controllers.routes.SessionExpiredController.onPageLoad().url)
      verify(mockUserAnswerRepository, times(1)).get(any())
    }
  }
}
