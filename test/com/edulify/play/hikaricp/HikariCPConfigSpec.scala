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
import org.specs2.mutable.Specification
import play.api.Configuration

class HikariCPConfigSpec extends Specification {

  "When reading configuration" should {

    "set dataSourceClassName when present" in {
      val properties = new Properties()
      properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
      properties.setProperty("username", "user")
      val config = new Configuration(ConfigFactory.parseProperties(properties))
      HikariCPConfig.toHikariConfig(config).getDataSourceClassName mustEqual "org.postgresql.ds.PGPoolingDataSource"
    }

    "set jdbcUrl when present" in {
      val properties = new Properties()
      properties.setProperty("jdbcUrl", "jdbc:postgresql://host/database")
      properties.setProperty("username", "user")
      val config = new Configuration(ConfigFactory.parseProperties(properties))
      HikariCPConfig.toHikariConfig(config).getJdbcUrl mustEqual "jdbc:postgresql://host/database"
    }

    "discard configuration not related to hikari config" in {
      val props = config().valid
      props.setProperty("just.some.garbage", "garbage")
      HikariCPConfig.toHikariConfig(config().configuration(props))
      success // won't fail because of garbage property
    }

    "throw exception when" in {
      "both dataSourceClassName and jdbcUrl are not present" in {
        val emptyProperties = config().invalid
        val configuration: Configuration = new Configuration(ConfigFactory.parseProperties(emptyProperties))
        HikariCPConfig.toHikariConfig(configuration) must throwA[IllegalArgumentException]
      }
      "username is not present" in {
        val props = config().invalid
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
        val configuration: Configuration = new Configuration(ConfigFactory.parseProperties(props))
        HikariCPConfig.toHikariConfig(configuration) must throwA[IllegalArgumentException]
      }
    }

    "respect the defaults as" in {
      "autoCommit to true" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isAutoCommit must beTrue
      }

      "connectionTimeout to 30 seconds" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getConnectionTimeout must beEqualTo(30.seconds.inMillis)
      }

      "idleTimeout to 10 minutes" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getIdleTimeout must beEqualTo(10.minutes.inMillis)
      }

      "maxLifetime to 30 minutes" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getMaxLifetime must beEqualTo(30.minutes.inMillis)
      }

      "validationTimeout to 5 seconds" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getValidationTimeout must beEqualTo(5.seconds.inMillis)
      }

      "minimumIdle to 10" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getMinimumIdle must beEqualTo(10)
      }

      "maximumPoolSize to 10" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getMaximumPoolSize must beEqualTo(10)
      }

      "initializationFailFast to true" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isInitializationFailFast must beTrue
      }

      "isolateInternalQueries to false" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isIsolateInternalQueries must beFalse
      }

      "allowPoolSuspension to false" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isAllowPoolSuspension must beFalse
      }

      "readOnly to false" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isReadOnly must beFalse
      }

      "registerMBeans to false" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).isRegisterMbeans must beFalse
      }

      "leakDetectionThreshold to 0 (zero)" in {
        val configuration = config().configuration(config().valid)
        HikariCPConfig.toHikariConfig(configuration).getLeakDetectionThreshold must beEqualTo(0)
      }
    }
  }
}

case class config() {
  def valid = {
    val properties = new Properties()
    properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
    properties.setProperty("username", "user")
    properties
  }

  def invalid = new Properties()

  def configuration(props: Properties) = new Configuration(ConfigFactory.parseProperties(props))
}