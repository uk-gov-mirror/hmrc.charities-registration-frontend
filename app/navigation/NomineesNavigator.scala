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

import config.FrontendAppConfig
import controllers.addressLookup.{routes => addressLookupRoutes}
import controllers.nominees.{routes => nomineeRoutes}
import controllers.routes
import javax.inject.Inject
import models._
import pages.Page
import pages.addressLookup.{NomineeIndividualAddressLookupPage, OrganisationNomineeAddressLookupPage}
import pages.nominees._
import play.api.mvc.Call

class NomineesNavigator @Inject()(implicit frontendAppConfig: FrontendAppConfig) extends BaseNavigator {

  private val normalRoutes: Page => UserAnswers => Call =  {

    case IsAuthoriseNomineePage => userAnswers: UserAnswers => userAnswers.get(IsAuthoriseNomineePage) match {
      case Some(true) => nomineeRoutes.ChooseNomineeController.onPageLoad(NormalMode)
      case Some(false) => nomineeRoutes.NomineeDetailsSummaryController.onPageLoad()
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case ChooseNomineePage => userAnswers: UserAnswers => userAnswers.get(ChooseNomineePage) match {
      case Some(true) => nomineeRoutes.IndividualNomineeNameController.onPageLoad(NormalMode)
      case Some(false) => nomineeRoutes.OrganisationNomineeNameController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineeNamePage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineeNamePage) match {
      case Some(_) => nomineeRoutes.IndividualNomineeDOBController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineeDOBPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineeDOBPage) match {
      case Some(_) => nomineeRoutes.IndividualNomineesPhoneNumberController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesPhoneNumberPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesPhoneNumberPage) match {
      case Some(_) => nomineeRoutes.IsIndividualNomineeNinoController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsIndividualNomineeNinoPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineeNinoPage) match {
      case Some(true) => nomineeRoutes.IndividualNomineesNinoController.onPageLoad(NormalMode)
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesNinoPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesNinoPage) match {
      case Some(_) => controllers.addressLookup.routes.NomineeIndividualAddressLookupController.initializeJourney(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsIndividualNomineePaymentsPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineePaymentsPage) match {
      case Some(true) => nomineeRoutes.IndividualNomineesBankAccountDetailsController.onPageLoad(NormalMode)
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesBankAccountDetailsPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesBankAccountDetailsPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case NomineeIndividualAddressLookupPage => userAnswers: UserAnswers =>
      userAnswers.get(NomineeIndividualAddressLookupPage) match {
        case Some(_) => nomineeRoutes.IsIndividualNomineePreviousAddressController.onPageLoad(NormalMode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }

    case IsIndividualNomineePreviousAddressPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineePreviousAddressPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => nomineeRoutes.IsIndividualNomineePaymentsController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeNamePage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeNamePage) match {
      case Some(_) => nomineeRoutes.OrganisationNomineeContactDetailsController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeContactDetailsPage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeContactDetailsPage) match {
      case Some(_) => addressLookupRoutes.OrganisationNomineeAddressLookupController.initializeJourney(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeAddressLookupPage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeAddressLookupPage) match {
      case Some(_) => nomineeRoutes.IsOrganisationNomineePreviousAddressController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsOrganisationNomineePreviousAddressPage => userAnswers: UserAnswers => userAnswers.get(IsOrganisationNomineePreviousAddressPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => nomineeRoutes.IsOrganisationNomineePaymentsController.onPageLoad(NormalMode)
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsOrganisationNomineePaymentsPage => userAnswers: UserAnswers => userAnswers.get(IsOrganisationNomineePaymentsPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case NomineeDetailsSummaryPage => _ => routes.IndexController.onPageLoad()

    case _ => _ => routes.IndexController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {

    case IsAuthoriseNomineePage => userAnswers: UserAnswers => userAnswers.get(IsAuthoriseNomineePage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => nomineeRoutes.NomineeDetailsSummaryController.onPageLoad()
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case ChooseNomineePage => userAnswers: UserAnswers => userAnswers.get(ChooseNomineePage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineeNamePage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineeNamePage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineeDOBPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineeDOBPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesPhoneNumberPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesPhoneNumberPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsIndividualNomineeNinoPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineeNinoPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesNinoPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesNinoPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsIndividualNomineePreviousAddressPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineePreviousAddressPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad()
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsIndividualNomineePaymentsPage => userAnswers: UserAnswers => userAnswers.get(IsIndividualNomineePaymentsPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IndividualNomineesBankAccountDetailsPage => userAnswers: UserAnswers => userAnswers.get(IndividualNomineesBankAccountDetailsPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeNamePage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeNamePage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case NomineeIndividualAddressLookupPage => userAnswers: UserAnswers =>
      userAnswers.get(NomineeIndividualAddressLookupPage) match {
        case Some(_) => routes.DeadEndController.onPageLoad()
        case _ => routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeContactDetailsPage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeContactDetailsPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case OrganisationNomineeAddressLookupPage => userAnswers: UserAnswers => userAnswers.get(OrganisationNomineeAddressLookupPage) match {
      case Some(_) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsOrganisationNomineePreviousAddressPage => userAnswers: UserAnswers => userAnswers.get(IsOrganisationNomineePreviousAddressPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case IsOrganisationNomineePaymentsPage => userAnswers: UserAnswers => userAnswers.get(IsOrganisationNomineePaymentsPage) match {
      case Some(true) => routes.DeadEndController.onPageLoad() // TODO next page
      case Some(false) => routes.DeadEndController.onPageLoad() // TODO next page
      case _ =>  routes.SessionExpiredController.onPageLoad()
    }

    case NomineeDetailsSummaryPage => _ => routes.IndexController.onPageLoad()

    case _ => _ => routes.IndexController.onPageLoad()
  }

  override def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
    case PlaybackMode =>
      routes.SessionExpiredController.onPageLoad() // TODO
  }
}
