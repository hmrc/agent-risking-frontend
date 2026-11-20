/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.agentriskingfrontend.shared.testdata

import uk.gov.hmrc.agentriskingfrontend.shared.*
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.LimitedCompany
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.Partnership.GeneralPartnership
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.Partnership.LimitedLiabilityPartnership
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.Partnership.LimitedPartnership
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.Partnership.ScottishLimitedPartnership
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.Partnership.ScottishPartnership
import uk.gov.hmrc.agentriskingfrontend.shared.BusinessType.SoleTrader
import uk.gov.hmrc.agentriskingfrontend.shared.agentdetails.*
import uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsDetails
import uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsRegistrationNumber
import uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsSupervisoryBodyCode
import uk.gov.hmrc.agentriskingfrontend.shared.audit.SessionId
import uk.gov.hmrc.agentriskingfrontend.shared.businessdetails.CompanyProfile
import uk.gov.hmrc.agentriskingfrontend.shared.businessdetails.FullName
import uk.gov.hmrc.agentriskingfrontend.shared.companieshouse.ChroAddress
import uk.gov.hmrc.agentriskingfrontend.shared.companieshouse.CompaniesHouseDateOfBirth
import uk.gov.hmrc.agentriskingfrontend.shared.companieshouse.CompaniesHouseNameQuery
import uk.gov.hmrc.agentriskingfrontend.shared.companieshouse.CompaniesHouseOfficer
import uk.gov.hmrc.agentriskingfrontend.shared.companieshouse.CompaniesHouseOfficerRole.LlpMember
import uk.gov.hmrc.agentriskingfrontend.shared.individual.*
import uk.gov.hmrc.agentriskingfrontend.shared.lists.FiveOrLess
import uk.gov.hmrc.agentriskingfrontend.shared.lists.FiveOrLessOfficers
import uk.gov.hmrc.agentriskingfrontend.shared.lists.IndividualName
import uk.gov.hmrc.agentriskingfrontend.shared.lists.SixOrMore
import uk.gov.hmrc.agentriskingfrontend.shared.lists.SixOrMoreOfficers
import uk.gov.hmrc.auth.core.ConfidenceLevel
import uk.gov.hmrc.auth.core.retrieve.Credentials

import java.time.*
import java.util.UUID

trait TdBase { dependencies: (TdTime) =>

  def randomId(): String = UUID.randomUUID().toString

  def cachedSessionId: SessionId = SessionId("session-id-123")
  def saUtr: SaUtr = SaUtr("1234567895")
  def ctUtr: CtUtr = CtUtr("2202108031")
  def internalUserId: InternalUserId = InternalUserId("internal-user-id-12345")
  def riskingCaseId: RiskingCaseId = RiskingCaseId("risking-case-id-12345")
  def linkId: LinkId = LinkId("link-id-12345")
  def groupId: GroupId = GroupId("group-id-12345")
  def credentials: Credentials = Credentials(
    providerId = "cred-id-12345",
    providerType = "GovernmentGateway"
  )
  def confidenceLevel250: ConfidenceLevel = ConfidenceLevel.L250
  def confidenceLevel50: ConfidenceLevel = ConfidenceLevel.L50

  def individualName = IndividualName("Test Name")
  def nino = Nino("AB123456C")
  def ninoFromAuth = IndividualNino.FromAuth(nino)
  def ninoProvided = IndividualNino.Provided(nino)
  def saUtrFromAuth = IndividualSaUtr.FromAuth(saUtr)
  def saUtrFromCitizenDetails = IndividualSaUtr.FromCitizenDetails(saUtr)
  def saUtrProvided = IndividualSaUtr.Provided(saUtr)

  def dateOfBirth: LocalDate = LocalDate.of(2000, 1, 1)
  def dateOfBirthFromCitizenDetails: IndividualDateOfBirth.FromCitizensDetails = IndividualDateOfBirth.FromCitizensDetails(dateOfBirth)
  def dateOfBirthProvided = IndividualDateOfBirth.Provided(dateOfBirth)
  def fullName: FullName = FullName(firstName = "Joe", lastName = "Bloggs")

  def individualEmailAddress: EmailAddress = EmailAddress("member@test.com")

  def telephoneNumber: TelephoneNumber = TelephoneNumber("(+44) 10794554342")
  def crn: Crn = Crn("1234567890")
  def agencyNameBase = "ABC Accounting"

  object TdAgencyName:

    val limitedCompany = s"$agencyNameBase LC"
    val limitedPartnership = s"$agencyNameBase LP"
    val llp = s"$agencyNameBase LLP"
    val scottishLimitedPartnership = s"$agencyNameBase SLP"
    val scottishPartnership = s"$agencyNameBase SP"
    val generalPartnership = s"$agencyNameBase GP"
    val soleTrader = s"$agencyNameBase ST"

    def getAgencyName(businessType: BusinessType): String =
      businessType match
        case LimitedCompany => limitedCompany
        case LimitedPartnership => limitedPartnership
        case LimitedLiabilityPartnership => llp
        case ScottishLimitedPartnership => scottishLimitedPartnership
        case ScottishPartnership => scottishPartnership
        case GeneralPartnership => generalPartnership
        case SoleTrader => soleTrader

  def personReference: PersonReference = PersonReference("1234567890")
  def getAgentBusinessName(businessType: BusinessType): AgentBusinessName = AgentBusinessName(
    agentBusinessName = TdAgencyName.getAgencyName(businessType),
    otherAgentBusinessName = None
  )

  def amlsCode: AmlsSupervisoryBodyCode = AmlsSupervisoryBodyCode("HMRC")
  def amlsRegistrationNumber: AmlsRegistrationNumber = AmlsRegistrationNumber("XAML00000123456")
  def vrn = Vrn("123456789")
  def payeRef = PayeRef("123/AB12345")
  def trn: String = "ST-TRN-987654321"
  def individualDateOfBirth: LocalDate = LocalDate.of(1980, 1, 1)
  def agentTelephoneNumber = AgentTelephoneNumber(agentTelephoneNumber = telephoneNumber.value, otherAgentTelephoneNumber = None)

  def getCompanyProfile(businessType: BusinessType): CompanyProfile = CompanyProfile(
    companyNumber = crn,
    companyName = TdAgencyName.getAgencyName(businessType),
    dateOfIncorporation = Some(dependencies.dateOfIncorporation),
    unsanitisedCHROAddress = Some(ChroAddress(
      address_line_1 = Some("23 Great Portland Street"),
      address_line_2 = Some("London"),
      postal_code = Some("W1 8LT"),
      country = Some("GB")
    ))
  )

  def postcode: String = "AA1 1AA"

  def agentVerifiedEmailAddress = AgentVerifiedEmailAddress(
    emailAddress = AgentEmailAddress(
      agentEmailAddress = EmailAddress(bprEmailAddress),
      otherAgentEmailAddress = None
    ),
    isVerified = true
  )
  def individualVerifiedEmailAddress = IndividualVerifiedEmailAddress(individualEmailAddress, isVerified = true)

  def getCompleteAgentDetails(businessType: BusinessType): AgentDetails = AgentDetails(
    agentCorrespondenceAddress = Some(chroAddress),
    telephoneNumber = Some(agentTelephoneNumber),
    agentEmailAddress = Some(agentVerifiedEmailAddress),
    businessName = getAgentBusinessName(businessType)
  )

  def completeAmlsDetails: AmlsDetails = AmlsDetails(
    supervisoryBody = amlsCode,
    amlsRegistrationNumber = Some(amlsRegistrationNumber),
    amlsEvidence = None
  )

  def completeAmlsDetailsAtt: AmlsDetails = AmlsDetails(
    supervisoryBody = AmlsSupervisoryBodyCode("ATT"),
    amlsRegistrationNumber = Some(AmlsRegistrationNumber("ATT AML-1-123456")),
    amlsEvidence = Some(uk.gov.hmrc.agentriskingfrontend.shared.amls.AmlsEvidence(
      uk.gov.hmrc.agentriskingfrontend.shared.upload.FileUploadReference("test-file-reference"),
      "evidence.pdf",
      uk.gov.hmrc.objectstore.client.Path.File("agent-registration-frontend/9d5ddeed-d26e-4005-97ca-e40f2466e0a3/evidence.pdf")
    ))
  )
  def chroAddress: AgentCorrespondenceAddress = AgentCorrespondenceAddress(
    addressLine1 = "23 Great Portland Street",
    addressLine2 = Some("London"),
    postalCode = Some("W1 8LT"),
    countryCode = "GB"
  )
  def individualProvidedDetailsId: IndividualProvidedDetailsId = IndividualProvidedDetailsId("individual-provided-details-id-12345")
  def bprPrimaryTelephoneNumber: String = "(+44) 78714743399"
  def newTelephoneNumber: String = "+44 (0) 7000000000"
  def bprEmailAddress: String = "bpr@example.com"
  def newEmailAddress: String = "new@example.com"

  def llpNameQuery = CompaniesHouseNameQuery(
    firstName = "Jane",
    lastName = "Leadenhall-Lane"
  )
  def companiesHouseOfficer = CompaniesHouseOfficer(
    name = "Taylor Leadenhall-Lane",
    dateOfBirth = Some(CompaniesHouseDateOfBirth(
      day = Some(12),
      month = 11,
      year = 1990
    )),
    resignedOn = None,
    officerRole = Some(LlpMember),
    identification = None
  )

  def fiveOrFewerKeyIndividuals: FiveOrLess = FiveOrLess(
    numberOfKeyIndividuals = 3
  )

  def fiveOrLessCompaniesHouseOfficers: FiveOrLessOfficers = FiveOrLessOfficers(
    numberOfCompaniesHouseOfficers = 1,
    isCompaniesHouseOfficersListCorrect = true
  )

  def twoCompaniesHouseOfficers: FiveOrLessOfficers = FiveOrLessOfficers(
    numberOfCompaniesHouseOfficers = 2,
    isCompaniesHouseOfficersListCorrect = true
  )

  def sixOrMoreKeyIndividuals: SixOrMore = SixOrMore(
    numberOfKeyIndividualsResponsibleForTaxMatters = Some(3)
  )

  def sixOrMoreCompaniesHouseOfficers: SixOrMoreOfficers = SixOrMoreOfficers(
    numberOfCompaniesHouseOfficers = 6,
    numberOfOfficersResponsibleForTaxMatters = 4
  )

  def sixCompaniesHouseOfficersSelectAll: SixOrMoreOfficers = SixOrMoreOfficers(
    numberOfCompaniesHouseOfficers = 6,
    numberOfOfficersResponsibleForTaxMatters = 6
  )

  /** This is a list of individual names that we currently have stubbed in companies house, We need to use this list for fast forward links to ensure the names
    * match the names we get from companies house stub
    */
  val individualNamesStubbedInCompaniesHouse: List[IndividualName] = List(
    IndividualName("Steve Austin"),
    IndividualName("Beverly Hills"),
    IndividualName("Pauline Austin"),
    IndividualName("Justine Hills"),
    IndividualName("Steve Palmer"),
    IndividualName("Sandra Hills")
  )

  def getIndividualName(
    index: Int
  ): IndividualName = individualNamesStubbedInCompaniesHouse.lift(index).getOrElse(throw new RuntimeException(s"Missing individual name for index $index"))

  def ucrIdentifiers: UcrIdentifiers = UcrIdentifiers(
    vrns = List(vrn),
    payeRefs = List(payeRef)
  )

  def emptyUcrIdentifiers: UcrIdentifiers = UcrIdentifiers(
    vrns = List.empty,
    payeRefs = List.empty
  )

  def arn: Arn = Arn("TARN0000001")

}
