import sbt._

object AppDependencies {

  def apply(): Seq[ModuleID] = compile ++ test

  private val bootstrapVersion    = "8.4.0"
  private val commonDomainVersion = "0.10.0"

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30"          % bootstrapVersion,
    "uk.gov.hmrc" %% "api-platform-test-common-domain" % commonDomainVersion
  ).map(_ % "test")
}
