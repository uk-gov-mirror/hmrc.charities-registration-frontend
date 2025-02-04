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

package config

import java.net.URLEncoder

import com.google.inject.{Inject, Singleton}
import play.api.i18n.Lang
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.util.Try

@Singleton
class FrontendAppConfig @Inject()(val servicesConfig: ServicesConfig) {

  lazy val host: String = servicesConfig.getString("host")
  lazy val appName: String = servicesConfig.getString("appName")
  lazy val govUK: String = servicesConfig.getString("urls.govUK")

  private val contactHost: String = servicesConfig.getString("contact-frontend.host")

  val contactFormServiceIdentifier: String = "iCharities"

  lazy val contactUrl: String = s"$contactHost/contact/contact-hmrc?service=$contactFormServiceIdentifier"

  val gtmContainer: String = servicesConfig.getString("tracking-consent-frontend.gtm.container")

  def feedbackUrl: String =
    s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier"

  def feedbackUnauthenticatedUrl: String =
    s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier"

  private val exitSurveyHost: String = servicesConfig.getString("feedback-frontend.host")

  def exitSurveyUrl: String = s"$exitSurveyHost/feedback/CHARITIES"

  lazy val loginUrl: String = servicesConfig.getString("urls.login")
  lazy val signOutUrl: String = servicesConfig.getString("urls.signOut")
  lazy val loginContinueUrl: String = servicesConfig.getString("urls.loginContinue")
  lazy val loginContinueKey: String = servicesConfig.getString("urls.continue")

  lazy val timeout: Int = servicesConfig.getInt("timeout.timeout")
  lazy val countdown: Int = servicesConfig.getInt("timeout.countdown")

  // Address lookup
  lazy val addressLookupFrontend: String = servicesConfig.baseUrl("address-lookup-frontend")
  lazy val retrieveAddressUrl: String = addressLookupFrontend + "/api/v2/confirmed"

  def feedbackUrlAddressLookup: String =
    s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier"

  //Footer Links
  lazy val cookies: String = host + servicesConfig.getString("urls.footer.cookies")
  lazy val privacy: String = host + servicesConfig.getString("urls.footer.privacy")
  lazy val termsConditions: String = host + servicesConfig.getString("urls.footer.termsConditions")
  lazy val govUKHelp: String = servicesConfig.getString("urls.footer.govukHelp")
  lazy val accessibilityStatement: String = host + servicesConfig.getString("urls.footer.accessibilityStatement")
  lazy val platformHost: String = Try(servicesConfig.getString("platform.frontend.host")).getOrElse("")

  def accessibilityStatementFrontendUrl()(implicit request: Request[_]): String = {
    s"$accessibilityStatement?referrerUrl=${URLEncoder.encode(s"$platformHost${request.path}", "UTF-8")}"
  }

  def languageTranslationEnabled: Boolean = servicesConfig.getBoolean("features.welshLanguage")
  lazy val isExternalTest: Boolean = servicesConfig.getBoolean("features.isExternalTest")
  lazy val noEmailPost: Boolean = servicesConfig.getBoolean("features.noEmailPost")

  def languageMap: Map[String, Lang] = Map("en" -> Lang("en"), "cy" -> Lang("cy"))

  lazy val getRecognition: String = servicesConfig.getString("urls.getRecognition")

  lazy val getCharitiesBackend: String = servicesConfig.baseUrl("charities")

  lazy val save4laterCacheBaseUrl: String = servicesConfig.baseUrl("cachable.short-lived-cache")
  lazy val save4laterDomain: String = servicesConfig.getConfString("cachable.short-lived-cache.domain", "save4later")

  lazy val timeToLiveInDays: Int = servicesConfig.getInt("user-answers.timeToLiveInDays")

}
