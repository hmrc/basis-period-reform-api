import uk.gov.hmrc.DefaultBuildSettings._

lazy val appName = "basis-period-reform-api"

lazy val plugins: Seq[Plugins]         = Seq(PlayScala, SbtDistributablesPlugin)
lazy val playSettings: Seq[Setting[_]] = Seq.empty

scalaVersion := "2.13.12"

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
ThisBuild / semanticdbEnabled                                    := true
ThisBuild / semanticdbVersion                                    := scalafixSemanticdb.revision

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    majorVersion := 0,
    targetJvm    := "jvm-11",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += "-Wconf:src=routes/.*:s"
  )
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    Test / unmanagedResourceDirectories += baseDirectory.value / "testcommon" / "resources"
  )
  .configs(IntegrationTest)
  .settings(integrationTestSettings())
  .settings(
    IntegrationTest / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    IntegrationTest / unmanagedResourceDirectories += baseDirectory.value / "testcommon" / "resources"
  )
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(CodeCoverageSettings.settings)

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: "it:test" :: state },
  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  // Coverage does not need compile !
  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "coverage" :: "run-all-tests" :: "coverageReport" :: "coverageOff" :: state }
)

Global / bloopAggregateSourceDependencies := true
