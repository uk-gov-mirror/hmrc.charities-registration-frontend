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

package forms.common

import forms.behaviours.StringFieldBehaviours
import models.Name
import play.api.data.{Form, FormError}

class NameFormProviderSpec extends StringFieldBehaviours {

  private val maxLength = 100
  private val messagePrefix: String = "authorisedOfficialsName"
  private val formProvider: NameFormProvider = inject[NameFormProvider]
  private val form: Form[Name] = formProvider(messagePrefix)

  ".firstName" must {

    val fieldName = "firstName"
    val requiredKey = s"$messagePrefix.firstName.error.required"
    val lengthKey = s"$messagePrefix.firstName.error.length"
    val invalidKey = s"$messagePrefix.firstName.error.format"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      nonEmptyString
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    behave like fieldWithRegex(
      form,
      fieldName,
      "()invalidFirstName",
      FormError(fieldName, invalidKey, Seq(formProvider.validateFields))
    )
  }

  ".middleName" must {

    val fieldName = "middleName"
    val lengthKey = s"$messagePrefix.middleName.error.length"
    val invalidKey = s"$messagePrefix.middleName.error.format"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      nonEmptyString
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like fieldWithRegex(
      form,
      fieldName,
      "CName&",
      FormError(fieldName, invalidKey, Seq(formProvider.validateFields))
    )
  }

  ".lastName" must {

    val fieldName = "lastName"
    val requiredKey = s"$messagePrefix.lastName.error.required"
    val lengthKey = s"$messagePrefix.lastName.error.length"
    val invalidKey = s"$messagePrefix.lastName.error.format"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      nonEmptyString
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    behave like fieldWithRegex(
      form,
      fieldName,
      "()invalidLastName",
      FormError(fieldName, invalidKey, Seq(formProvider.validateFields))
    )
  }
  
  "AuthorisedOfficialsNameFormProvider" must {

    val authorisedOfficialsName = Name("Jim", Some("John"), "Jones")

    "apply AuthorisedOfficialsName correctly" in {

      val details = form.bind(
        Map(
          "firstName" -> authorisedOfficialsName.firstName,
          "middleName" -> authorisedOfficialsName.middleName.getOrElse(""),
          "lastName" -> authorisedOfficialsName.lastName
        )
      ).get

      details.firstName mustBe authorisedOfficialsName.firstName
      details.middleName mustBe authorisedOfficialsName.middleName
      details.lastName mustBe authorisedOfficialsName.lastName
    }

    "unapply AuthorisedOfficialsName correctly" in {
      val filled = form.fill(authorisedOfficialsName)
      filled("firstName").value.value mustBe authorisedOfficialsName.firstName
      filled("middleName").value.value mustBe authorisedOfficialsName.middleName.get
      filled("lastName").value.value mustBe authorisedOfficialsName.lastName
    }
  }

  "firstName" must {

    "valid for firstName" in {

      "firstName" must fullyMatch regex formProvider.validateFields
    }

    "valid for firstName&" in {

      "firstName&" mustNot fullyMatch regex formProvider.validateFields
    }
  }

  "middleName" must {

    "valid for middleName" in {

      "middleName" must fullyMatch regex formProvider.validateFields
    }

    "valid for middleName&" in {

      "middleName&" mustNot fullyMatch regex formProvider.validateFields
    }
  }

  "lastName" must {

    "valid for lastName" in {

      "OpName" must fullyMatch regex formProvider.validateFields
    }

    "valid for lastName&" in {

      "lastName&" mustNot fullyMatch regex formProvider.validateFields
    }
  }
}
