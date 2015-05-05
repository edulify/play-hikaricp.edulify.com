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
import java.net.URI

object HikariCPConfig {

  def toHikariConfig(dataSourceName: String, config: Configuration): HikariConfig = {
    val hikariConfig = new HikariConfig()

    // Essentials configurations
    config.getString("dataSourceClassName") match {
      case Some(className) => hikariConfig.setDataSourceClassName(className)
      case None => Logger.debug("`dataSourceClassName` not present. Will use `jdbcUrl` instead.")
    }

    config.getString("jdbcUrl").orElse(config.getString("url")) match {
      case Some(jdbcUrl) => hikariConfig.setJdbcUrl(jdbcUrl)
      case None => Logger.debug("`jdbcUrl` not present. Pool configured from `databaseUrl`.")
    }

    config.getString("databaseUrl") match {
      case Some(databaseUrl) => {
        val dbUri = new URI(databaseUrl)
        val dbScheme = dbUri.getScheme match {
          case "postgres" => "postgresql"
          case scheme => scheme
        }
        hikariConfig.setJdbcUrl(s"jdbc:${dbScheme}://${dbUri.getHost}:${dbUri.getPort}${dbUri.getPath}")
        hikariConfig.setUsername(dbUri.getUserInfo.split(":")(0))
        hikariConfig.setPassword(dbUri.getUserInfo.split(":")(1))
      }
      case None => Logger.debug("`databaseUrl` not present. Will use `dataSourceClassName` instead.")
    }

    config.getConfig("dataSource").foreach { dataSourceConfig =>
      dataSourceConfig.keys.foreach { key =>
        hikariConfig.addDataSourceProperty(key, dataSourceConfig.getString(key).get)
      }
    }

    config.getString("username").orElse(config.getString("user")).foreach(hikariConfig.setUsername)
    config.getString("password").foreach(hikariConfig.setPassword)

    config.getString("driverClassName").orElse(config.getString("driver")).foreach(hikariConfig.setDriverClassName)

    // Frequently used
    config.getBoolean("autoCommit").foreach(hikariConfig.setAutoCommit)
    config.getMilliseconds("connectionTimeout").foreach(hikariConfig.setConnectionTimeout)
    config.getMilliseconds("idleTimeout").foreach(hikariConfig.setIdleTimeout)
    config.getMilliseconds("maxLifetime").foreach(hikariConfig.setMaxLifetime)
    config.getString("connectionTestQuery").foreach(hikariConfig.setConnectionTestQuery)
    config.getInt("minimumIdle").foreach(hikariConfig.setMinimumIdle)
    config.getInt("maximumPoolSize").foreach(hikariConfig.setMaximumPoolSize)
    hikariConfig.setPoolName(config.getString("poolName").getOrElse(dataSourceName))

    // Infrequently used
    config.getBoolean("initializationFailFast").foreach(hikariConfig.setInitializationFailFast)
    config.getBoolean("isolateInternalQueries").foreach(hikariConfig.setIsolateInternalQueries)
    config.getBoolean("allowPoolSuspension").foreach(hikariConfig.setAllowPoolSuspension)
    config.getBoolean("readOnly").foreach(hikariConfig.setReadOnly)
    config.getBoolean("registerMbeans").foreach(hikariConfig.setRegisterMbeans)
    config.getString("catalog").foreach(hikariConfig.setCatalog)
    config.getString("connectionInitSql").foreach(hikariConfig.setConnectionInitSql)
    config.getString("transactionIsolation").foreach(hikariConfig.setTransactionIsolation)
    config.getMilliseconds("validationTimeout").foreach(hikariConfig.setValidationTimeout)
    config.getMilliseconds("leakDetectionThreshold").foreach(hikariConfig.setLeakDetectionThreshold)

    hikariConfig
  }
}
