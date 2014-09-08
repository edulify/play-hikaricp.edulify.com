name := "play-hikaricp"

version := "1.5.0"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  "com.zaxxer" % "HikariCP-java6" % "2.0.1",
  "commons-configuration" % "commons-configuration" % "1.10",
  "commons-collections" % "commons-collections" % "3.2.1"
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

homepage := Some(url("http://edulify.github.io/play-hikaricp.edulify.com/"))

pomExtra := (
  <scm>
    <url>https://github.com/edulify/play-hikaricp.edulify.com</url>
    <connection>scm:git:git@github.com:edulify/play-hikaricp.edulify.com.git</connection>
    <developerConnection>scm:git:https://github.com/edulify/play-hikaricp.edulify.com.git</developerConnection>
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

