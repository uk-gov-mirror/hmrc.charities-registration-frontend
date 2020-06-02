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

import forms.behaviours.OptionFieldBehaviours
import models.regulators.SelectWhyNoRegulator
import play.api.data.FormError

class SelectWhyNoRegulatorFormProviderSpec extends OptionFieldBehaviours {

  val form = new SelectWhyNoRegulatorFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "selectWhyNoRegulator.error.required"

    behave like optionsField[SelectWhyNoRegulator](
      form,
      fieldName,
      validValues  = SelectWhyNoRegulator.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
