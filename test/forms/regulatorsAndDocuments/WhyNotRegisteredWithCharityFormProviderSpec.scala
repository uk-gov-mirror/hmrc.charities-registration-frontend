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

package forms.regulatorsAndDocuments

import forms.behaviours.StringFieldBehaviours
import play.api.data.{Form, FormError}

class WhyNotRegisteredWithCharityFormProviderSpec extends StringFieldBehaviours {

  private val formProvider: WhyNotRegisteredWithCharityFormProvider = inject[WhyNotRegisteredWithCharityFormProvider]
  private val form: Form[String] = formProvider()

  ".value" must {

    val requiredKey = "whyNotRegisteredWithCharity.error.required"
    val lengthKey = "whyNotRegisteredWithCharity.error.length"
    val invalidKey = "whyNotRegisteredWithCharity.error.format"
    val maxLength = 100
    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      nonEmptyString
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
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
      "abc@&",
      FormError(fieldName, invalidKey, Seq(formProvider.validateField))
    )
  }

  "validatereason" must {

    "valid for abcd" in {

      "abcd" must fullyMatch regex formProvider.validateField
    }

    "valid for abc@" in {

      "abc@" mustNot fullyMatch regex formProvider.validateField
    }
  }
}
