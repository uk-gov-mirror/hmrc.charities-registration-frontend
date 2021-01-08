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

package controllers.nominees

import base.SpecBase
import controllers.actions.{AuthIdentifierAction, FakeAuthIdentifierAction}
import models.addressLookup.{AddressModel, CountryModel}
import models.{Name, NormalMode, SelectTitle, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import pages.addressLookup.NomineeIndividualPreviousAddressLookupPage
import pages.nominees.IndividualNomineeNamePage
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import repositories.UserAnswerRepository
import views.html.common.ConfirmAddressView

import scala.concurrent.Future

class ConfirmNomineeIndividualPreviousAddressControllerSpec extends SpecBase with BeforeAndAfterEach {

  override lazy val userAnswers: Option[UserAnswers] = Some(emptyUserAnswers)

  override def applicationBuilder(): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[UserAnswerRepository].toInstance(mockUserAnswerRepository),
        bind[AuthIdentifierAction].to[FakeAuthIdentifierAction]
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockUserAnswerRepository)
  }

  private val view: ConfirmAddressView = injector.instanceOf[ConfirmAddressView]
  private val controller: ConfirmNomineeIndividualPreviousAddressController = inject[ConfirmNomineeIndividualPreviousAddressController]
  private val messageKeyPrefix = "nomineeIndividualPreviousAddress"
  private val nomineeIndividualPreviousAddressLookup = List("12", "Banner Way", "United Kingdom")

  "ConfirmNomineeIndividualPreviousAddressController Controller" must {

    "return OK and the correct view for a GET" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers
        .set(IndividualNomineeNamePage, Name(SelectTitle.Mr, "Jim", Some("John"), "Jones"))
        .flatMap(_.set(NomineeIndividualPreviousAddressLookupPage, AddressModel(List("12", "Banner Way"), None, CountryModel("GB", "United Kingdom"))))
        .success.value)))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view.apply(
        nomineeIndividualPreviousAddressLookup, messageKeyPrefix,
        controllers.nominees.routes.IsIndividualNomineePaymentsController.onPageLoad(NormalMode),
        controllers.addressLookup.routes.NomineeIndividualPreviousAddressLookupController.initializeJourney(NormalMode), Some("Jim John Jones"))(fakeRequest, messages, frontendAppConfig).toString
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

    "redirect to Session Expired for a GET if no data found for address" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(Some(emptyUserAnswers)))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      when(mockUserAnswerRepository.get(any())).thenReturn(Future.successful(None))

      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url
      verify(mockUserAnswerRepository, times(1)).get(any())
    }

  }
}
