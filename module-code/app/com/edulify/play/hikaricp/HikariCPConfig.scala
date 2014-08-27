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

object HikariCPConfig {
  lazy val DEFAULT_DATASOURCE_NAME = "default"
  lazy val HIKARI_CP_PROPERTIES_FILE = "hikaricp.properties"

  def getHikariConfig(dbConfig: Configuration) = {
    val file = new File(HIKARI_CP_PROPERTIES_FILE)
    if(file.exists()) new HikariConfig(HikariCPConfig.props(file))
    else new HikariConfig(HikariCPConfig.mapFromPlayConfiguration(dbConfig))
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

    logProperties(properties)
    properties
  }

  private def mapFromPlayConfiguration(dbConfig: Configuration): Properties = {
    Logger.info("Loading Hikari configuration from Play configuration.")

    val configFile = dbConfig.getString("hikaricp.file")
    if(configFile.nonEmpty) {
      Logger.info("Loading from file configured by db.default.hikaricp.file that is " + configFile)
      return props(new File(configFile.get))
    }

    val properties = new ConfigProperties(dbConfig)
    properties.setPropertyFromConfig("driverClassName", "driver")
    properties.setPropertyFromConfig("jdbcUrl",         "url")
    properties.setPropertyFromConfig("username",        "user")
    properties.setPropertyFromConfig("password",        "password")

    properties.setProperty("maximumPoolSize",        maxPoolSize(dbConfig))
    properties.setProperty("minimumIdle",            minPoolSize(dbConfig))
    properties.setProperty("maxLifetime",            maxLifetime(dbConfig))
    properties.setProperty("idleTimeout",            idleTimeout(dbConfig))
    properties.setProperty("connectionTimeout",      connectionTimeout(dbConfig))
    properties.setProperty("leakDetectionThreshold", leakDetectionThreshold(dbConfig))

    properties.setPropertyFromConfig("catalog",             "defaultCatalog")
    properties.setPropertyFromConfig("autoCommit",          "defaultAutoCommit", "true")
    properties.setPropertyFromConfig("connectionTestQuery", "connectionTestStatement", "")
    properties.setProperty("jdbc4ConnectionTest", properties.getProperty("connectionTestQuery"))
    properties.setPropertyFromConfig("transactionIsolation", "defaultTransactionIsolation")
    properties.setPropertyFromConfig("readOnly",             "defaultReadOnly", "false")

    properties.setPropertyFromConfig("registerMbeans",       "statisticsEnabled", "false")
    properties.setPropertyFromConfig("connectionInitSql",    "initSQL")

    logProperties(properties)
    properties
  }

  private def logProperties(properties: Properties): Unit = {
    // Log the properties that are used, but don't print out the raw password for security-sake
    Logger.info("Properties: " + properties.asScala.map { case (name: String, value: String) =>
      if (name contains "password") {
        "%s=%.1s%s" format(name, value, value.substring(value.length).padTo(value.length - 1, "*").mkString)
      } else "%s=%s" format(name, value)
    }.mkString(", "))
  }

  private def maxPoolSize(config: Configuration) = {
    val partitionCount = config.getInt("partitionCount").getOrElse(1)
    val maxConnectionsPerPartition = config.getInt("maxConnectionsPerPartition").getOrElse(30)
    (partitionCount * maxConnectionsPerPartition).toString
  }

  private def minPoolSize(config: Configuration) = {
    val partitionCount = config.getInt("partitionCount").getOrElse(1)
    val maxConnectionsPerPartition = config.getInt("minConnectionsPerPartition").getOrElse(5)
    (partitionCount * maxConnectionsPerPartition).toString
  }

  private def maxLifetime(config: Configuration) = {
    var maxLife = config.getInt("maxConnectionAge").getOrElse(30)
    maxLife     = config.getInt("maxConnectionAgeInSeconds").getOrElse(maxLife) * 60 * 1000
    maxLife.toString
  }

  private def idleTimeout(config: Configuration) = {
    var idleMaxAge = config.getInt("idleMaxAge").getOrElse(10)
    idleMaxAge     = config.getInt("idleMaxAgeInMinutes").getOrElse(idleMaxAge) * 60
    idleMaxAge     = config.getInt("idleMaxAgeInSeconds").getOrElse(idleMaxAge) * 1000
    idleMaxAge.toString
  }

  private def connectionTimeout(config: Configuration) = {
    var timeout = config.getInt("connectionTimeout").getOrElse(30)
    timeout     = config.getInt("connectionTimeoutInMs").getOrElse(timeout) * 1000
    timeout.toString
  }

  private def leakDetectionThreshold(config: Configuration) = {
    var threshold = config.getInt("closeConnectionWatchTimeout").getOrElse(0)
    threshold     = config.getInt("closeConnectionWatchTimeoutInMs").getOrElse(threshold)
    threshold.toString
  }

  private class ConfigProperties(config: Configuration) extends Properties {
    // Keep track of the required fields which if not set cause the startup to fail
    val playRequired : Set[String] = Set("driver", "url", "user", "password")

    def setPropertyFromConfig(poolStr: String, playStr: String, default: String): Unit = {
      setProperty(poolStr, config.getString(playStr).getOrElse(default))
    }

    def setPropertyFromConfig(poolStr: String, playStr: String, required: Boolean = false): Unit = {
      var prop = config.getString(playStr)
      if (prop.nonEmpty) {
        setProperty(poolStr, prop.get)
      } else if(playRequired contains playStr) {
        throw config.reportError("Play Config", "Required property not found: '" + playStr + "'")
      }
    }
  }
}
