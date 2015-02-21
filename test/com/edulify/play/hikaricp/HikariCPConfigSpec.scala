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

import java.util.Properties

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.Configuration

class HikariCPConfigSpec extends Specification {

  "When reading configuration" should {

    "set dataSourceClassName when present" in {
      val properties = new Properties()
      properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
      properties.setProperty("username", "user")
      val config = new Configuration(ConfigFactory.parseProperties(properties))
      HikariCPConfig.toHikariConfig(config).getDataSourceClassName == "org.postgresql.ds.PGPoolingDataSource"
    }

    "set jdbcUrl when present" in {
      val properties = new Properties()
      properties.setProperty("jdbcUrl", "jdbc:postgresql://host/database")
      properties.setProperty("username", "user")
      val config = new Configuration(ConfigFactory.parseProperties(properties))
      HikariCPConfig.toHikariConfig(config).getJdbcUrl == "jdbc:postgresql://host/database"
    }

    "discard configuration not related to hikari config" in new Configs {
      val props = valid
      props.setProperty("just.some.garbage", "garbage")
      HikariCPConfig.toHikariConfig(asConfig(props))
      success // won't fail because of garbage property
    }

    "set dataSource sub properties" in new Configs {
      val props = valid
      props.setProperty("dataSource.user", "user")
      props.setProperty("dataSource.password", "password")
      val hikariConfig: HikariConfig = HikariCPConfig.toHikariConfig(asConfig(props))

      hikariConfig.getDataSourceProperties.getProperty("user") == "user"
      hikariConfig.getDataSourceProperties.getProperty("password") == "password"
    }

    "respect the defaults as" in {
      "autoCommit to true" in new Configs {
        HikariCPConfig.toHikariConfig(config).isAutoCommit must beTrue
      }

      "connectionTimeout to 30 seconds" in new Configs {
        HikariCPConfig.toHikariConfig(config).getConnectionTimeout == 30.seconds.inMillis
      }

      "idleTimeout to 10 minutes" in new Configs {
        HikariCPConfig.toHikariConfig(config).getIdleTimeout == 10.minutes.inMillis
      }

      "maxLifetime to 30 minutes" in new Configs {
        HikariCPConfig.toHikariConfig(config).getMaxLifetime == 30.minutes.inMillis
      }

      "validationTimeout to 5 seconds" in new Configs {
        HikariCPConfig.toHikariConfig(config).getValidationTimeout == 5.seconds.inMillis
      }

      "minimumIdle to 10" in new Configs {
        HikariCPConfig.toHikariConfig(config).getMinimumIdle == 10
      }

      "maximumPoolSize to 10" in new Configs {
        HikariCPConfig.toHikariConfig(config).getMaximumPoolSize == 10
      }

      "initializationFailFast to true" in new Configs {
        HikariCPConfig.toHikariConfig(config).isInitializationFailFast must beTrue
      }

      "isolateInternalQueries to false" in new Configs {
        HikariCPConfig.toHikariConfig(config).isIsolateInternalQueries must beFalse
      }

      "allowPoolSuspension to false" in new Configs {
        HikariCPConfig.toHikariConfig(config).isAllowPoolSuspension must beFalse
      }

      "readOnly to false" in new Configs {
        HikariCPConfig.toHikariConfig(config).isReadOnly must beFalse
      }

      "registerMBeans to false" in new Configs {
        HikariCPConfig.toHikariConfig(config).isRegisterMbeans must beFalse
      }

      "leakDetectionThreshold to 0 (zero)" in new Configs {
        HikariCPConfig.toHikariConfig(config).getLeakDetectionThreshold == 0
      }
    }

    "override the defaults for property" in {
      "autoCommit" in new Configs {
        val props = valid
        props.setProperty("autoCommit", "false")
        HikariCPConfig.toHikariConfig(asConfig(props)).isAutoCommit must beFalse
      }

      "connectionTimeout" in new Configs {
        val props = valid
        props.setProperty("connectionTimeout", "40 seconds")
        HikariCPConfig.toHikariConfig(asConfig(props)).getConnectionTimeout == 40.seconds.inMillis
      }

      "idleTimeout" in new Configs {
        val props = valid
        props.setProperty("idleTimeout", "5 minutes")
        HikariCPConfig.toHikariConfig(asConfig(props)).getIdleTimeout == 5.minutes.inMillis
      }

      "maxLifetime" in new Configs {
        val props = valid
        props.setProperty("maxLifetime", "15 minutes")
        HikariCPConfig.toHikariConfig(asConfig(props)).getMaxLifetime == 15.minutes.inMillis
      }

      "validationTimeout" in new Configs {
        val props = valid
        props.setProperty("validationTimeout", "10 seconds")
        HikariCPConfig.toHikariConfig(asConfig(props)).getValidationTimeout == 10.seconds.inMillis
      }

      "minimumIdle" in new Configs {
        val props = valid
        props.setProperty("minimumIdle", "20")
        HikariCPConfig.toHikariConfig(asConfig(props)).getMinimumIdle == 20
      }

      "maximumPoolSize" in new Configs {
        val props = valid
        props.setProperty("maximumPoolSize", "20")
        HikariCPConfig.toHikariConfig(asConfig(props)).getMaximumPoolSize == 20
      }

      "initializationFailFast" in new Configs {
        val props = valid
        props.setProperty("initializationFailFast", "false")
        HikariCPConfig.toHikariConfig(asConfig(props)).isInitializationFailFast must beFalse
      }

      "isolateInternalQueries" in new Configs {
        val props = valid
        props.setProperty("isolateInternalQueries", "true")
        HikariCPConfig.toHikariConfig(asConfig(props)).isIsolateInternalQueries must beTrue
      }

      "allowPoolSuspension" in new Configs {
        val props = valid
        props.setProperty("allowPoolSuspension", "true")
        HikariCPConfig.toHikariConfig(asConfig(props)).isAllowPoolSuspension must beTrue
      }

      "readOnly" in new Configs {
        val props = valid
        props.setProperty("readOnly", "true")
        HikariCPConfig.toHikariConfig(asConfig(props)).isReadOnly must beTrue
      }

      "leakDetectionThreshold" in new Configs {
        val props = valid
        props.setProperty("leakDetectionThreshold", "2")
        HikariCPConfig.toHikariConfig(asConfig(props)).getLeakDetectionThreshold == 0
      }
    }
  }
}

trait Configs extends Scope {
  val config = asConfig(valid)
  val invalid = new Properties()

  def valid = {
    val properties = new Properties()
    properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
    properties.setProperty("username", "user")
    properties
  }

  def asConfig(props: Properties) = new Configuration(ConfigFactory.parseProperties(props))
}