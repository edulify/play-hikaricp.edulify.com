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

    "discard configuration not related to hikari config" in {
      val props = Configurations().valid
      props.setProperty("just.some.garbage", "garbage")
      HikariCPConfig.toHikariConfig(Configurations().configuration(props))
      success // won't fail because of garbage property
    }

    "set dataSource sub properties" in {
      val props = Configurations().valid
      props.setProperty("dataSource.user", "user")
      props.setProperty("dataSource.password", "password")
      val hikariConfig: HikariConfig = HikariCPConfig.toHikariConfig(Configurations().configuration(props))

      hikariConfig.getDataSourceProperties.getProperty("user") == "user"
      hikariConfig.getDataSourceProperties.getProperty("password") == "password"
    }

    "respect the defaults as" in {
      "autoCommit to true" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isAutoCommit must beTrue
      }

      "connectionTimeout to 30 seconds" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getConnectionTimeout == 30.seconds.inMillis
      }

      "idleTimeout to 10 minutes" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getIdleTimeout == 10.minutes.inMillis
      }

      "maxLifetime to 30 minutes" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getMaxLifetime == 30.minutes.inMillis
      }

      "validationTimeout to 5 seconds" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getValidationTimeout == 5.seconds.inMillis
      }

      "minimumIdle to 10" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getMinimumIdle == 10
      }

      "maximumPoolSize to 10" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getMaximumPoolSize == 10
      }

      "initializationFailFast to true" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isInitializationFailFast must beTrue
      }

      "isolateInternalQueries to false" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isIsolateInternalQueries must beFalse
      }

      "allowPoolSuspension to false" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isAllowPoolSuspension must beFalse
      }

      "readOnly to false" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isReadOnly must beFalse
      }

      "registerMBeans to false" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).isRegisterMbeans must beFalse
      }

      "leakDetectionThreshold to 0 (zero)" in new ValidConfig {
        HikariCPConfig.toHikariConfig(config).getLeakDetectionThreshold == 0
      }
    }
  }
}

trait ValidConfig extends Scope {
  val config = new Configuration(ConfigFactory.parseProperties(Configurations().valid))
}

case class Configurations() {
  def valid = {
    val properties = new Properties()
    properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
    properties.setProperty("username", "user")
    properties
  }

  def invalid = new Properties()

  def configuration(props: Properties) = new Configuration(ConfigFactory.parseProperties(props))
}