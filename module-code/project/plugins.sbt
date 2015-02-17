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

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % System.getProperty("play.version", "2.3.8"))

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")

