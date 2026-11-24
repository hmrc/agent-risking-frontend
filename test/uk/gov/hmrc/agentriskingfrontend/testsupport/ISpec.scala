/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.agentriskingfrontend.testsupport

import com.google.inject.AbstractModule
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.Logging
import play.api.Mode
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.guice.GuiceableModule
import play.api.test.DefaultTestServerFactory
import play.api.test.TestServerFactory
import play.core.server.ServerConfig
import uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsName
import uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsSupervisoryBodyCode
import uk.gov.hmrc.agentriskingfrontend.shared.individual.IndividualProvidedDetailsId
import uk.gov.hmrc.agentriskingfrontend.shared.upload.UploadId
import uk.gov.hmrc.agentriskingfrontend.shared.*
import uk.gov.hmrc.agentriskingfrontend.config.AmlsCodes
import uk.gov.hmrc.agentriskingfrontend.config.CsvLoader
import uk.gov.hmrc.agentriskingfrontend.models.upscan.UploadIdGenerator
import uk.gov.hmrc.agentriskingfrontend.shared.individual.IndividualProvidedDetailsIdGenerator
import uk.gov.hmrc.agentriskingfrontend.testsupport.testdata.TdAll
import uk.gov.hmrc.agentriskingfrontend.testsupport.wiremock.WireMockSupport

import java.time.Clock
import java.time.Instant
import scala.concurrent.ExecutionContext

trait ISpec
extends AnyWordSpecLike,
  RichMatchers,
  BeforeAndAfterEach,
  GuiceOneServerPerSuite,
  WireMockSupport:

//  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = scaled(Span(3, Seconds)), interval = scaled(Span(300, Millis)))
  private val testServerPort = ISpec.testServerPort
  val thisFrontendBaseUrl: String = ISpec.thisFrontendBaseUrl

  lazy val tdAll: TdAll = TdAll.tdAll

  /** The moment this spec runs at.
    *
    * Defaults to the test data's "now", which is some days after the application was created, so a fixture part-way through the journey has its earlier events
    * in the past. A spec that drives the production code into stamping a timestamp of its own has to override this with the moment of that event, or the
    * stamped value and the fixture's will not agree.
    */
  protected def frozenInstant: Instant = tdAll.now

  given clock: Clock = Clock.fixed(frozenInstant, tdAll.zoneId)
  given ExecutionContext = app.injector.instanceOf[ExecutionContext]

  protected final val AppRoutes = uk.gov.hmrc.agentriskingfrontend.controllers.AppRoutes // alias so no need to import it everywhere in ISpecs

  protected def configMap: Map[String, Any] =
    Map[String, Any](
      "urls.this-frontend" -> thisFrontendBaseUrl,
      "urls.this-frontend-relative" -> thisFrontendBaseUrl,
      "microservice.services.object-store.port" -> WireMockSupport.port,
      "microservice.services.agent-risking-backend.port" -> WireMockSupport.port,
      "microservice.services.auth.port" -> WireMockSupport.port,
      "microservice.services.companies-house-api-proxy.port" -> WireMockSupport.port,
      "microservice.services.email-verification.port" -> WireMockSupport.port,
      "microservice.services.enrolment-store-proxy.port" -> WireMockSupport.port,
      "microservice.services.upscan-initiate.port" -> WireMockSupport.port,
      "microservice.services.citizen-details.port" -> WireMockSupport.port,
      "microservice.services.address-lookup-frontend.port" -> WireMockSupport.port,
      "microservice.services.agent-assurance.port" -> WireMockSupport.port,
      "play.http.router" -> "testOnlyDoNotUseInAppConf.Routes",
      "auditing.consumer.baseUri.port" -> WireMockSupport.port,
      "auditing.enabled" -> false,
      "auditing.traceRequests" -> false,
      "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
      "features.grs-stub" -> false,
      "inject-email-verification-passcodes-page" -> false, // false to match production behavior where email verification is performed without injected test only page
      "fieldLevelEncryption.enabled" -> true,
      "fieldLevelEncryption.key" -> "edkOOwt7uvzw1TXnFIN6aRVHkfWcgiOrbBvkEQvO65g=" // use base64 even for tests
    ) ++ configOverrides

  protected def configOverrides: Map[String, Any] = Map[String, Any]()

  lazy val overridesModule: AbstractModule =
    new AbstractModule:
      override def configure(): Unit =
        bind(classOf[Clock]).toInstance(clock)
        bind(classOf[AmlsCodes]).toInstance(
          new AmlsCodes {
            override val amlsCodes: Map[AmlsSupervisoryBodyCode, AmlsName] = CsvLoader.load("/testAmlsCodes.csv").map: kv =>
              (AmlsSupervisoryBodyCode(kv._1), AmlsName(kv._2))
          }
        )
        bind(classOf[LinkIdGenerator]).toInstance(new LinkIdGenerator {
          override def nextLinkId(): LinkId = tdAll.linkId
        })
        bind(classOf[RiskingCaseIdGenerator]).toInstance(new RiskingCaseIdGenerator {
          override def nextRiskingCaseId(): RiskingCaseId = tdAll.riskingCaseId
        })
        bind(classOf[UploadIdGenerator]).toInstance(new UploadIdGenerator {
          override def nextUploadId(): UploadId = tdAll.uploadId
        })
        bind(classOf[IndividualProvidedDetailsIdGenerator]).toInstance(new IndividualProvidedDetailsIdGenerator {
          override def nextIndividualProvidedDetailsId(): IndividualProvidedDetailsId = tdAll.individualProvidedDetailsId
        })
        bind(classOf[PersonReferenceGenerator]).toInstance(new PersonReferenceGenerator {
          override def generatePersonReference(): PersonReference = tdAll.personReference
        })

  override def fakeApplication(): Application = GuiceApplicationBuilder()
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridesModule)))
    .configure(configMap).build()

  override protected def testServerFactory: TestServerFactory = CustomTestServerFactory

  object CustomTestServerFactory
  extends DefaultTestServerFactory:
    override protected def serverConfig(app: Application): ServerConfig =
      val sc = ServerConfig(
        port = Some(testServerPort),
        sslPort = None,
        mode = Mode.Test,
        rootDir = app.path
      )
      sc.copy(configuration = sc.configuration.withFallback(overrideServerConfiguration(app)))

  override def beforeEach(): Unit = super.beforeEach()

object ISpec
extends Logging:

  val testServerPort: Int = 19004
  val defaultServerPort: Int = 22204
  val thisFrontendBaseUrl: String = s"http://localhost:$testServerPort"
  val selfBaseUrl: String = s"http://localhost:$defaultServerPort"
