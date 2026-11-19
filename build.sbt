val strictBuilding: SettingKey[Boolean] = StrictBuilding.strictBuilding //defining here so it can be set before running sbt like `sbt 'set Global / strictBuilding := true' ...`
StrictBuilding.strictBuildingSetting
val playPort: Int = 22204

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.7.0"
ThisBuild / scalafmtOnCompile := true
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixOnCompile := true

lazy val microservice = Project("agent-risking-frontend", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    pipelineStages := Seq(gzip),
    Compile / doc / scalacOptions := Seq(), // this will allow to have warnings in `doc` task
    Test / doc / scalacOptions := Seq(), // this will allow to have warnings in `doc` task
    scalacOptions ++= ScalaCompilerFlags.scalaCompilerOptions,
    scalacOptions ++= {
      if (StrictBuilding.strictBuilding.value)
        ScalaCompilerFlags.strictScalaCompilerOptions
      else
        Nil
    },
    Test / parallelExecution := true,
    routesImport ++= Seq(
      "uk.gov.hmrc.agentriskingfrontend",
      "uk.gov.hmrc.agentriskingfrontend.RoutesExports.*",
      "uk.gov.hmrc.agentriskingfrontend.RoutesExports.given"
    )
  )
  .settings(CodeCoverageSettings.settings)
  .settings(commands ++= SbtCommands.commands)
  .settings(SbtUpdatesSettings.sbtUpdatesSettings *)
  .settings(WartRemoverSettings.wartRemoverSettings)
  .settings(PlayKeys.playDefaultPort := playPort)
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.agentriskingfrontend.controllers.AppRoutes",
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._"
    )
  )
