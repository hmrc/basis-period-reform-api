import sbt._

object AppDependencies {

  def apply(): Seq[ModuleID] = compile ++ test

  private val bootstrapVersion = "10.7.0"

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30"  % bootstrapVersion,
    "org.mockito" %% "mockito-scala-scalatest" % "2.2.1"
  ).map(_ % "test")
}
