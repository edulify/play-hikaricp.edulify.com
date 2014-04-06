import play.Project._

name := "play-hirakicp"

version := "0.0.3"

libraryDependencies ++= Seq(
  jdbc,
  "com.zaxxer" % "HikariCP" % "1.3.5"
)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

organization := "com.edulify"

organizationName := "Edulify.com"

organizationHomepage := Some(new URL("https://edulify.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

startYear := Some(2012)

description := "HikariCP Plugin for Play Framework 2.2.x"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("http://blabluble.github.io/play-hikaricp.edulify.com/"))

pomExtra := (
  <scm>
    <url>https://github.com/blabluble/play-hikaricp.edulify.com</url>
    <connection>scm:git:git@github.com:blabluble/play-hikaricp.edulify.com.git</connection>
    <developerConnection>scm:git:https://github.com/blabluble/play-hikaricp.edulify.com.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>megazord</id>
        <name>Megazord</name>
        <email>contact [at] edulify.com</email>
        <url>https://github.com/megazord</url>
      </developer>
    </developers>
  )

scalacOptions := Seq("-feature", "-deprecation")

playScalaSettings
