/*
 * Copyright (C) 2013 FURYU CORPORATION
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appOrganization = "com.edulify"
  val appName         = "play-hirakicp"
  val appVersion      = "0.0.2"
  val appScalaVersion = "2.10.3"
  val appScalaCrossVersions = Seq(appScalaVersion, "2.9.3")

  val main = Project(appName, base = file(".")).settings(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    crossScalaVersions := appScalaCrossVersions,
    resolvers ++= Seq(
      "Maven Snapshot Repository" at "http://people.apache.org/maven-snapshot-repository/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    libraryDependencies <+= scalaVersion(v => {
      v match {
        case "2.9.1" | "2.9.2" | "2.9.3" => "com.typesafe.play" %% "play" % "[2.2.0,2.2.2]" % "provided"
        case _ => "com.typesafe.play" % "play-jdbc" % "[2.2.0,2.2.2]" % "provided" cross CrossVersion.binaryMapped {
          case "2.10.0" => "2.10"
          case x => x
        }
      }
    }),
    libraryDependencies ++= Seq(
      "com.zaxxer" % "HikariCP" % "1.3.5"
    )
  )
}