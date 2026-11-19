import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  private val bootstrapVersion = "10.9.0"
  private val hmrcMongoVersion = "2.14.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30" % "13.17.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-30"         % hmrcMongoVersion,
    "com.softwaremill.quicklens" %% "quicklens" % "1.9.15",
    "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "3.5.0",
    "uk.gov.hmrc.objectstore" %% "object-store-client-play-30" % "2.6.0",
    "uk.gov.hmrc" %% "crypto-json-play-30" % "8.4.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootstrapVersion            % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % hmrcMongoVersion            % Test,
    "org.jsoup"               %  "jsoup"                      % "1.24.1"            % Test,
  )
}
