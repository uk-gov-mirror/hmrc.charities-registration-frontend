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

package models.oldCharities

import org.joda.time.{LocalDate, MonthDay}
import play.api.libs.json._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

// scalastyle:off number.of.types

case class CharityContactDetails(fullName: String, operatingName: Option[String], daytimePhone: String,
  mobilePhone: Option[String], email: Option[String], website: Option[String])

object CharityContactDetails {

  implicit val formats: OFormat[CharityContactDetails] = Json.format[CharityContactDetails]
}

case class CharityAddress(addressLine1:String, addressLine2:String, addressLine3:String, addressLine4:String,
   postcode:String, country:String)

object CharityAddress {

  implicit val formats: OFormat[CharityAddress] = Json.format[CharityAddress]
}

case class OptionalCharityAddress(toggle: Option[String], address: CharityAddress)

object OptionalCharityAddress {

  implicit val formats: OFormat[OptionalCharityAddress] = Json.format[OptionalCharityAddress]
}

class RegulatorDetailsBase(baseisCharityRegulatorSelected: Boolean, baseCharityRegistrationNumber: String )

case class CharityReasonForNotRegistering(charityRegulator: Option[String], notRegReasonOtherDescription:Option[String])

object CharityReasonForNotRegistering {

  implicit val formats: OFormat[CharityReasonForNotRegistering] = Json.format[CharityReasonForNotRegistering]
}

case class CharityRegulatorInfoDetails (isCharityRegulatorSelected: Boolean,charityRegistrationNumber: String )
  extends RegulatorDetailsBase(baseisCharityRegulatorSelected = isCharityRegulatorSelected,
    baseCharityRegistrationNumber = charityRegistrationNumber)


object CharityRegulatorInfoDetails {

  implicit val formats: OFormat[CharityRegulatorInfoDetails] = Json.format[CharityRegulatorInfoDetails]
}

case class CharityRegulatorOtherInfoDetails (isCharityOtherRegulatorSelected: Boolean,charityRegulatorName: String,
  charityOtherRegistrationNumber: String) extends RegulatorDetailsBase(
    baseisCharityRegulatorSelected = isCharityOtherRegulatorSelected,
    baseCharityRegistrationNumber = charityOtherRegistrationNumber)


object CharityRegulatorOtherInfoDetails {

  implicit val formats: OFormat[CharityRegulatorOtherInfoDetails] = Json.format[CharityRegulatorOtherInfoDetails]
}
case class CharityRegulator(ccew: CharityRegulatorInfoDetails, oscr: CharityRegulatorInfoDetails,
  ccni: CharityRegulatorInfoDetails, other: CharityRegulatorOtherInfoDetails,
  reasonForNotRegistering:CharityReasonForNotRegistering)


object CharityRegulator {

  implicit val formats: OFormat[CharityRegulator] = Json.format[CharityRegulator]
}

case class CharityGoverningDocument(docType: String, nameOtherDoc: String, govDocApprovedWording: String,
  effectiveDate: Option[LocalDate], governingApprovedDoc : Option[Boolean])


object CharityGoverningDocument {

  implicit val formats: OFormat[CharityGoverningDocument] = Json.format[CharityGoverningDocument]
}

case class ScalaMonthDay(monthDay: MonthDay)

object ScalaMonthDay {

  implicit val MonthDayReads: Reads[ScalaMonthDay] = (jv: JsValue) => {
    JsSuccess(ScalaMonthDay(new MonthDay((jv \ "monthInYear").as[Int], (jv \ "dayInMonth").as[Int])))
  }

  implicit val MonthDayWrites: Writes[ScalaMonthDay] = (mt: ScalaMonthDay) =>
    JsString(s"--${mt.monthDay.getMonthOfYear}-${mt.monthDay.getDayOfMonth}")
}

case class OperationAndFundsCommon(accountPeriodEnd: ScalaMonthDay, financialAccounts: Option[Boolean],
  bankStatements: Option[Boolean], noBankStatements: Option[String])

object OperationAndFundsCommon {

  implicit val formats: OFormat[OperationAndFundsCommon] = Json.format[OperationAndFundsCommon]
}

case class FutureFunds(donations: Boolean, fundraising: Boolean, grants: Boolean, membershipSubscriptions: Boolean,
  tradingIncome: Boolean, tradingSubsidiaries: Boolean, investmentIncome: Boolean, other: Boolean)

object FutureFunds {

  implicit val formats: OFormat[FutureFunds] = Json.format[FutureFunds]
}

case class WhereWillCharityOperate(englandAndWales: Boolean, scotland: Boolean, northernIreland: Boolean,
  ukWide: Boolean, overseas: Boolean)

object WhereWillCharityOperate {

  implicit val formats: OFormat[WhereWillCharityOperate] = Json.format[WhereWillCharityOperate]
}

case class OtherCountriesOfOperation(overseas1: Option[String], overseas2: Option[String], overseas3: Option[String],
  overseas4: Option[String], overseas5: Option[String])

object OtherCountriesOfOperation {

  implicit val formats: OFormat[OtherCountriesOfOperation] = Json.format[OtherCountriesOfOperation]
}

case class OperationAndFunds(operationAndFundsCommon: OperationAndFundsCommon, futureFunds: FutureFunds,
  futureFundsOther: Option[String], estimatedGrossIncome: Option[Int], incomeReceivedToDate: Option[Int],
  otherAreaOperation: Option[Boolean], whereWillCharityOperate: WhereWillCharityOperate,
  otherCountriesOfOperation: OtherCountriesOfOperation)

object OperationAndFunds {

  implicit val formats: OFormat[OperationAndFunds] = Json.format[OperationAndFunds]
}

case class WhatYourCharityDoes(charitableObjectives :String, reliefOfPoverty: Boolean, education: Boolean,
  animalWelfare: Boolean, healthOrSavingOfLives: Boolean, citizenshipOrCommunityDevelopment: Boolean,
  reliefOfThoseInNeed: Boolean, religion: Boolean, amateurSport: Boolean, humanRights: Boolean,
  artsCultureHeritageOrScience: Boolean, environmentalProtectionOrImprovement: Boolean,
  promotionOfEfficiencyInArmedForcesPoliceFireAndRescueService: Boolean, whatYourCharityDoesOther: Boolean,
  whatYourCharityDoesOtherReason: String, charityThingsBenefitThePublic :String)

object WhatYourCharityDoes {

  implicit val formats: OFormat[WhatYourCharityDoes] = Json.format[WhatYourCharityDoes]
}

case class CharityBankAccountDetails(accountName: String, accountNumber: String, sortCode: String, rollNumber: Option[String])

object CharityBankAccountDetails {

  implicit val formats: OFormat[CharityBankAccountDetails] = Json.format[CharityBankAccountDetails]
}
