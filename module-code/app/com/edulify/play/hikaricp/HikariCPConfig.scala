/*
 * Copyright 2014 Edulify.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edulify.play.hikaricp

import com.zaxxer.hikari.HikariConfig
import play.api.{Configuration, Logger}

import java.io.{File, IOException}
import java.util.Properties

import scala.collection.JavaConverters._

import org.apache.commons.configuration.{PropertiesConfiguration, ConfigurationConverter}

class HikariCPConfig(dbConfig: Configuration) {
  lazy val DEFAULT_DATASOURCE_NAME = "default"
  lazy val HIKARI_CP_PROPERTIES_FILE = "hikaricp.properties"

  def getHikariConfig = {
    val file = new File(HIKARI_CP_PROPERTIES_FILE)
    if(file.exists()) new HikariConfig(props(file))
    else new HikariConfig(mapFromPlayConfiguration())
  }

  private def props(file: File): Properties = {
    Logger.info("Loading Hikari configuration from " + file)

    var properties = new Properties()
    try {
      properties = ConfigurationConverter.getProperties(new PropertiesConfiguration(file))
    } catch {
      case ex: IOException =>
        play.api.Logger.warn("Could not read file " + file, ex)
    }

    Logger.info("Properties: " + properties.asScala.map { case (name: String, value: String) =>
      if (name contains "password") {
        "%s=%.1s%s" format(name, value, value.substring(value.length).padTo(value.length - 1, "*").mkString)
      } else "%s=%s" format(name, value)
    }.mkString(", "))

    properties
  }

  private def mapFromPlayConfiguration(): Properties = {
    Logger.info("Loading Hikari configuration from Play configuration.")

    val configFile = dbConfig.getString("hikaricp.file")
    if(configFile.nonEmpty) {
      Logger.info("Loading from file configured by db.default.hikaricp.file that is " + configFile)
      return props(new File(configFile.get))
    }

    val properties = new Properties()
    properties.setProperty("driverClassName",   dbConfig.getString("driver").get)
    properties.setProperty("jdbcUrl",           dbConfig.getString("url").get)
    properties.setProperty("username",          dbConfig.getString("user").get)
    properties.setProperty("password",          dbConfig.getString("password").get)

    properties.setProperty("maximumPoolSize",   maxPoolSize(dbConfig))
    properties.setProperty("minimumIdle",       minPoolSize(dbConfig))

    properties.setProperty("maxLifetime",            maxLifetime(dbConfig))
    properties.setProperty("idleTimeout",            idleTimeout(dbConfig))
    properties.setProperty("connectionTimeout",      connectionTimeout(dbConfig))
    properties.setProperty("leakDetectionThreshold", leakDetectionThreshold(dbConfig))

    properties.setProperty("catalog",              dbConfig.getString("defaultCatalog").get)
    properties.setProperty("autoCommit",           dbConfig.getString("defaultAutoCommit").getOrElse("true"))
    properties.setProperty("connectionTestQuery",  dbConfig.getString("connectionTestStatement").get)
    properties.setProperty("jdbc4ConnectionTest",  (dbConfig.getString("connectionTestStatement").get == null).toString)
    properties.setProperty("transactionIsolation", dbConfig.getString("defaultTransactionIsolation").get)
    properties.setProperty("readOnly",             dbConfig.getString("defaultReadOnly").getOrElse("false"))

    properties.setProperty("registerMbeans",    dbConfig.getString("statisticsEnabled").getOrElse("false"))
    properties.setProperty("connectionInitSql", dbConfig.getString("initSQL").get)

    Logger.info("Properties: " + properties)
    properties
  }

  private def maxPoolSize(config: Configuration) = {
    val partitionCount = config.getInt("partitionCount").getOrElse(1)
    val maxConnectionsPerPartition = config.getInt("maxConnectionsPerPartition").get
    (partitionCount * maxConnectionsPerPartition).toString
  }

  private def minPoolSize(config: Configuration) = {
    val partitionCount = config.getInt("partitionCount").getOrElse(1)
    val maxConnectionsPerPartition = config.getInt("minConnectionsPerPartition").get
    (partitionCount * maxConnectionsPerPartition).toString
  }

  private def maxLifetime(config: Configuration) = {
    var maxLife = dbConfig.getInt("maxConnectionAge").getOrElse(30)
    maxLife     = dbConfig.getInt("maxConnectionAgeInSeconds").getOrElse(maxLife) * 60 * 1000
    maxLife.toString
  }

  private def idleTimeout(config: Configuration) = {
    var idleMaxAge = dbConfig.getInt("idleMaxAge").getOrElse(10)
    idleMaxAge     = dbConfig.getInt("idleMaxAgeInMinutes").getOrElse(idleMaxAge) * 60
    idleMaxAge     = dbConfig.getInt("idleMaxAgeInSeconds").getOrElse(idleMaxAge) * 1000
    idleMaxAge.toString
  }

  private def connectionTimeout(config: Configuration) = {
    var timeout = dbConfig.getInt("connectionTimeout").getOrElse(30000)
    timeout     = dbConfig.getInt("connectionTimeoutInMs").getOrElse(timeout);
    timeout.toString
  }

  private def leakDetectionThreshold(config: Configuration) = {
    var threshold = dbConfig.getInt("closeConnectionWatchTimeout").getOrElse(0)
    threshold     = dbConfig.getInt("closeConnectionWatchTimeoutInMs").getOrElse(threshold)
    threshold.toString
  }
}
