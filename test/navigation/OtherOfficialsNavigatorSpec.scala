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

package navigation

import java.time.LocalDate

import base.SpecBase
import controllers.addressLookup.{routes => addressLookupRoutes}
import controllers.otherOfficials.{routes => otherOfficialRoutes}
import controllers.routes
import models.authOfficials.OfficialsPosition
import models.addressLookup.{AddressModel, CountryModel}
import models.{CheckMode, Index, Name, NormalMode, PhoneNumber, PlaybackMode, SelectTitle}
import pages.IndexPage
import pages.addressLookup.OtherOfficialAddressLookupPage
import pages.otherOfficials._
import play.api.mvc.Call

class OtherOfficialsNavigatorSpec extends SpecBase {

  private val navigator: OtherOfficialsNavigator = inject[OtherOfficialsNavigator]
  private val otherOfficialsName: Name = Name(SelectTitle.Mr, "Jim", Some("John"), "Jones")
  private val otherOfficialsPhoneNumber: PhoneNumber = PhoneNumber("07700 900 982", "07700 900 982")
  private val address: AddressModel = AddressModel(Seq("7", "Morrison street"), Some("G58AN"), CountryModel("UK", "United Kingdom"))
  private val minYear = 16

  "Navigator.nextPage(page, mode, userAnswers)" when {

    "in Normal mode" when {

      "from the OtherOfficialsNamePage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsNamePage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [Full name]'s date of birth? when save and continue button clicked" in {
          navigator.nextPage(OtherOfficialsNamePage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsNamePage(0), otherOfficialsName).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.OtherOfficialsDOBController.onPageLoad(NormalMode, Index(0))
        }
      }

      "from the OtherOfficialsDOBPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsDOBPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [full name]'s phone number? when save and continue button clicked" in {
          navigator.nextPage(OtherOfficialsDOBPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsDOBPage(0), LocalDate.now().minusYears(minYear)).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.OtherOfficialsPhoneNumberController.onPageLoad(NormalMode, Index(0))
        }
      }

      "from the OtherOfficialsPhoneNumberPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPhoneNumberPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [full name]'s position in charity? page when clicked continue button" in {
          navigator.nextPage(OtherOfficialsPhoneNumberPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPhoneNumberPage(0), otherOfficialsPhoneNumber).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.OtherOfficialsPositionController.onPageLoad(NormalMode, Index(0))
        }
      }

      "from the OtherOfficialsPositionPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPositionPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to Does [Full name] have a National Insurance number? when clicked continue button" in {
          navigator.nextPage(OtherOfficialsPositionPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPositionPage(0), OfficialsPosition.BoardMember).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.IsOtherOfficialNinoController.onPageLoad(NormalMode, Index(0))
        }
      }

      "from the IsOtherOfficialNinoPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(IsOtherOfficialNinoPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [full name]’s National Insurance number? when yes is selected" in {
          navigator.nextPage(IsOtherOfficialNinoPage(0), NormalMode,
            emptyUserAnswers.set(IsOtherOfficialNinoPage(0),true).success.value) mustBe
            otherOfficialRoutes.OtherOfficialsNinoController.onPageLoad(NormalMode, Index(0))
        }

        "go to the DeadEnd page when no is selected" in {
          navigator.nextPage(IsOtherOfficialNinoPage(0), NormalMode,
            emptyUserAnswers.set(IsOtherOfficialNinoPage(0),false).success.value) mustBe
            routes.DeadEndController.onPageLoad()
        }
      }

      "from the OtherOfficialsNinoPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsNinoPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [Full name]’s home address? when clicked continue button" in {
          navigator.nextPage(OtherOfficialsNinoPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsNinoPage(0), "QQ 12 34 56 C").getOrElse(emptyUserAnswers)) mustBe
            addressLookupRoutes.OtherOfficialsAddressLookupController.initializeJourney(Index(0), NormalMode)
        }
      }

      "from the OtherOfficialAddressLookupPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialAddressLookupPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the Has [Full name]’s home address changed in the last 12 months? page when clicked continue button" in {
          navigator.nextPage(OtherOfficialAddressLookupPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialAddressLookupPage(0), address).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.OtherOfficialsPreviousAddressController.onPageLoad(NormalMode, Index(0))
        }
      }

      "from the OtherOfficialsPreviousAddress Page" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(0), NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to previous address lookup flow when Yes is selected" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad()
        }

        "go to you have added one other official when No is selected and index is 0" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(0), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), false).getOrElse(emptyUserAnswers)) mustBe
            otherOfficialRoutes.AddedOneOtherOfficialController.onPageLoad()
        }

        "go to you have added two other official when No is selected and index is 1" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(1), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(1), false)).success.value) mustBe
            otherOfficialRoutes.AddedSecondOtherOfficialController.onPageLoad()
        }

        "go to you have added three other official when No is selected and index is 2" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(2), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(1), true)).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(2), false)).success.value) mustBe
            otherOfficialRoutes.AddedThirdOtherOfficialController.onPageLoad()
        }

        "go to you have added three other official when No is selected and index is 3" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(3), NormalMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(1), true)).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(2), false)).flatMap
            (_.set(OtherOfficialsPreviousAddressPage(3), false)).success.value) mustBe
            routes.DeadEndController.onPageLoad() // TODO three summary page
        }
      }

      "from the AddedOneOtherOfficialPage" must {

        "go to the AddSecondOtherOfficials page when user answer is empty" in {
          navigator.nextPage(AddedOneOtherOfficialPage, NormalMode, emptyUserAnswers) mustBe
            otherOfficialRoutes.AddSecondOtherOfficialsController.onPageLoad()
        }
      }

      "from the AddedSecondOtherOfficialPage" must {

        "go to the DoYouWantToAddAnotherOtherOfficial page when user answer is empty" in {
          navigator.nextPage(AddedSecondOtherOfficialPage, NormalMode, emptyUserAnswers) mustBe
            otherOfficialRoutes.AddAnotherOtherOfficialController.onPageLoad(NormalMode)
        }
      }

      "from the AddedThirdOtherOfficialPage" must {

        "go to the Summary page when user answer is empty" in {
          navigator.nextPage(AddedThirdOtherOfficialPage, NormalMode, emptyUserAnswers) mustBe
            routes.DeadEndController.onPageLoad()
        }
      }

      "from the AddAnotherOtherOfficialsPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(AddAnotherOtherOfficialPage, NormalMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the OtherOfficialsNameController page when yes is selected" in {
          navigator.nextPage(AddAnotherOtherOfficialPage, NormalMode,
            emptyUserAnswers.set(AddAnotherOtherOfficialPage, true).success.value) mustBe
            otherOfficialRoutes.OtherOfficialsNameController.onPageLoad(NormalMode,2)
        }

        "go to the summary page when no is selected" in {
          navigator.nextPage(AddAnotherOtherOfficialPage, NormalMode,
            emptyUserAnswers.set(AddAnotherOtherOfficialPage, false).success.value) mustBe
            otherOfficialRoutes.OtherOfficialsSummaryController.onPageLoad()
        }
      }

      "from any UnKnownPage" must {

        "go to the IndexController page when user answer is empty" in {
          navigator.nextPage(IndexPage, NormalMode, emptyUserAnswers) mustBe
            routes.IndexController.onPageLoad()
        }
      }
    }

    "in Check mode" when {

      "from the OtherOfficialsNamePage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsNamePage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the dead-end when save and continue button is clicked" in {
          navigator.nextPage(OtherOfficialsNamePage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsNamePage(0), otherOfficialsName).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the OtherOfficialsDOBPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsDOBPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the dead-end when save and continue button is clicked" in {
          navigator.nextPage(OtherOfficialsDOBPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsDOBPage(0), LocalDate.now().minusYears(minYear)).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the OtherOfficialsPhoneNumberPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPhoneNumberPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the dead-end when save and continue button is clicked" in {
          navigator.nextPage(OtherOfficialsPhoneNumberPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsPhoneNumberPage(0), otherOfficialsPhoneNumber).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the OtherOfficialsPositionPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPositionPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the dead-end when save and continue button is clicked" in {
          navigator.nextPage(OtherOfficialsPositionPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsPositionPage(0), OfficialsPosition.BoardMember).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the IsOtherOfficialNinoPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(IsOtherOfficialNinoPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the DeadEnd page when no is selected" in {
          navigator.nextPage(IsOtherOfficialNinoPage(0), CheckMode,
            emptyUserAnswers.set(IsOtherOfficialNinoPage(0),false).success.value) mustBe
            routes.DeadEndController.onPageLoad()
        }
      }

      "from the OtherOfficialsNinoPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsNinoPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the What is [Full name]’s home address? when clicked continue button" in {
          navigator.nextPage(OtherOfficialsNinoPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsNinoPage(0), "QQ 12 34 56 C").getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the OtherOfficialAddressLookupPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialAddressLookupPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the summary page when continue button is clicked" in {
          navigator.nextPage(OtherOfficialAddressLookupPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialAddressLookupPage(0), address).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from the OtherOfficialsPreviousAddress Page" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(0), CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the summary page when clicked continue button" in {
          navigator.nextPage(OtherOfficialsPreviousAddressPage(0), CheckMode,
            emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is created
        }
      }

      "from the AddAnotherOtherOfficialsPage" must {

        "go to the SessionExpiredController page when user answer is empty" in {
          navigator.nextPage(AddAnotherOtherOfficialPage, CheckMode, emptyUserAnswers) mustBe
            routes.SessionExpiredController.onPageLoad()
        }

        "go to the summary page when clicked continue button" in {
          navigator.nextPage(AddAnotherOtherOfficialPage, CheckMode,
            emptyUserAnswers.set(AddAnotherOtherOfficialPage, true).getOrElse(emptyUserAnswers)) mustBe
            routes.DeadEndController.onPageLoad() // TODO when summary page is ready
        }
      }

      "from any UnKnownPage" must {

        "go to the IndexController page when user answer is empty" in {
          navigator.nextPage(IndexPage, CheckMode, emptyUserAnswers) mustBe
            routes.IndexController.onPageLoad()
        }
      }
    }

    def goToPlaybackPage(index: Int): Call = index match {
      case 0 => otherOfficialRoutes.AddedOneOtherOfficialController.onPageLoad()
      case 1 => otherOfficialRoutes.AddedSecondOtherOfficialController.onPageLoad()
      case 2 => otherOfficialRoutes.AddedThirdOtherOfficialController.onPageLoad()
      case _ => routes.SessionExpiredController.onPageLoad()
    }

     def previousOrSameIndex(index: Int): Int = index match {
       case nonZero: Int if nonZero > 0 => nonZero - 1
       case _ => 0
     }

    List(0,1,2).foreach(index => {
      s"in Playback mode for index $index" when {

        "from the OtherOfficialsNamePage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsNamePage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the summary page when continue button is clicked" in {
            navigator.nextPage(OtherOfficialsNamePage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsNamePage(0), otherOfficialsName)
                .flatMap(_.set(OtherOfficialsNamePage(previousOrSameIndex(index)),otherOfficialsName))
                .flatMap(_.set(OtherOfficialsNamePage(index), otherOfficialsName)).getOrElse(emptyUserAnswers)) mustBe
              goToPlaybackPage(index)
          }
        }

        "from the OtherOfficialsDOBPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsDOBPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the summary page when continue button is clicked" in {
            navigator.nextPage(OtherOfficialsDOBPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsDOBPage(0), LocalDate.now().minusYears(minYear))
                .flatMap(_.set(OtherOfficialsDOBPage(previousOrSameIndex(index)),LocalDate.now().minusYears(minYear)))
                .flatMap(_.set(OtherOfficialsDOBPage(index), LocalDate.now().minusYears(minYear))).getOrElse(emptyUserAnswers)) mustBe
              goToPlaybackPage(index)
          }
        }

        "from the OtherOfficialsPhoneNumberPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsPhoneNumberPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the summary page when continue button is clicked" in {
            navigator.nextPage(OtherOfficialsPhoneNumberPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsPhoneNumberPage(0), otherOfficialsPhoneNumber)
                .flatMap(_.set(OtherOfficialsPhoneNumberPage(previousOrSameIndex(index)),otherOfficialsPhoneNumber))
                .flatMap(_.set(OtherOfficialsPhoneNumberPage(index), otherOfficialsPhoneNumber)).getOrElse(emptyUserAnswers)) mustBe
              goToPlaybackPage(index)
          }
        }

        "from the OtherOfficialsPositionPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsPositionPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the summary page when continue button is clicked" in {
            navigator.nextPage(OtherOfficialsPositionPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsPositionPage(0), OfficialsPosition.BoardMember)
                .flatMap(_.set(OtherOfficialsPositionPage(previousOrSameIndex(index)), OfficialsPosition.BoardMember))
                .flatMap(_.set(OtherOfficialsPositionPage(index), OfficialsPosition.BoardMember)).getOrElse(emptyUserAnswers)) mustBe
              goToPlaybackPage(index)
          }
        }

        "from the IsOtherOfficialNinoPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(IsOtherOfficialNinoPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the summary page when continue button is clicked" in {
            navigator.nextPage(IsOtherOfficialNinoPage(index), PlaybackMode,
              emptyUserAnswers.set(IsOtherOfficialNinoPage(0), true)
                .flatMap(_.set(IsOtherOfficialNinoPage(previousOrSameIndex(index)), true))
                .flatMap(_.set(IsOtherOfficialNinoPage(index), true)).getOrElse(emptyUserAnswers)) mustBe
              routes.DeadEndController.onPageLoad() // TODO when next page is ready
          }
        }

        "from the OtherOfficialsNINOPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsNinoPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the What is [Full name]’s home address? when clicked continue button" in {
            navigator.nextPage(OtherOfficialsNinoPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsNinoPage(0), "QQ 12 34 56 C")
                .flatMap(_.set(OtherOfficialsNinoPage(previousOrSameIndex(index)), "QQ 12 34 56 C"))
                .flatMap(_.set(OtherOfficialsNinoPage(index), "QQ 12 34 56 C")).getOrElse(emptyUserAnswers)) mustBe
              goToPlaybackPage(index)
          }
        }

        "from the OtherOfficialAddressLookupPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialAddressLookupPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the Have you previously changed your address page when continue button is clicked" in {
            navigator.nextPage(OtherOfficialAddressLookupPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialAddressLookupPage(0), address)
                .flatMap(_.set(OtherOfficialAddressLookupPage(previousOrSameIndex(index)), address))
                .flatMap(_.set(OtherOfficialAddressLookupPage(index), address)).getOrElse(emptyUserAnswers)) mustBe
              otherOfficialRoutes.OtherOfficialsPreviousAddressController.onPageLoad(PlaybackMode, index)
          }
        }

        "from the OtherOfficialPreviousAddressPage" must {

          "go to the SessionExpiredController page when user answer is empty" in {
            navigator.nextPage(OtherOfficialsPreviousAddressPage(index), PlaybackMode, emptyUserAnswers) mustBe
              routes.SessionExpiredController.onPageLoad()
          }

          "go to the Previous Address Lookup flow when yes is selected" in {
            navigator.nextPage(OtherOfficialsPreviousAddressPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), true)
                .flatMap(_.set(OtherOfficialsPreviousAddressPage(previousOrSameIndex(index)), true))
                .flatMap(_.set(OtherOfficialsPreviousAddressPage(index), true)).success.value) mustBe
              routes.DeadEndController.onPageLoad() // TODO when next page is ready
          }

          "go to the You have added one other official page when no is selected" in {
            navigator.nextPage(OtherOfficialsPreviousAddressPage(index), PlaybackMode,
              emptyUserAnswers.set(OtherOfficialsPreviousAddressPage(0), false)
                .flatMap(_.set(OtherOfficialsPreviousAddressPage(previousOrSameIndex(index)), false))
                .flatMap(_.set(OtherOfficialsPreviousAddressPage(index), false)).success.value) mustBe
              goToPlaybackPage(index)
          }

        }
      }
    })
  }
}
