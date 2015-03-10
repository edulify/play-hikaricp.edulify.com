import sbt._

ivyLoggingLevel := UpdateLogging.Full

// Comment to get more information during initialization
logLevel := Level.Info

resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    Classpaths.typesafeReleases
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % System.getProperty("play.version", "2.3.8"))

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.7")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0.BETA1")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.0.3")
