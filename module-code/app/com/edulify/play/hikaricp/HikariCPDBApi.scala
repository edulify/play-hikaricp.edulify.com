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

import java.sql.{Driver, DriverManager}
import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import play.api.db.DBApi
import play.api.libs.JNDI
import play.api.{Configuration, Logger}

class HikariCPDBApi(configuration: Configuration, classloader: ClassLoader) extends DBApi {

  lazy val dataSourceConfigs = configuration.subKeys.map {
    dataSourceName => dataSourceName -> configuration.getConfig(dataSourceName).getOrElse(Configuration.empty)
  }

  val datasources: List[(DataSource, String)] = dataSourceConfigs.map {
    case (dataSourceName, dataSourceConfig) =>
      val hikariConfig = HikariCPConfig.getHikariConfig(dataSourceConfig)
      registerDriver(dataSourceConfig)
      val dataSource = new HikariDataSource(hikariConfig)
      bindToJNDI(dataSourceConfig, hikariConfig, dataSource)
      dataSource -> dataSourceName
  }.toList

  def shutdownPool(ds: DataSource) = {
    play.api.Logger.info("Shutting down connection pool.")
    ds match {
      case ds: HikariDataSource => ds.shutdown()
    }
  }

  def getDataSource(name: String): DataSource = {
    val dataSource = datasources.find(tuple => tuple._2 == name)
      .map(element => element._1)
      .getOrElse(sys.error(" - could not find datasource for name " + name))
    dataSource
  }

  private def registerDriver(config: Configuration): Unit = {
    val driver = config.getString("driver")
    if (driver.isEmpty) return
    try {
      val driverClassName = driver.get
      Logger.info("Registering driver " + driverClassName)
      DriverManager.registerDriver(new play.utils.ProxyDriver(Class.forName(driverClassName, true, classloader).newInstance.asInstanceOf[Driver]))
    } catch {
      case t: Throwable => throw config.reportError("driver", "Driver not found: [" + driver + "]", Some(t))
    }
  }

  private def bindToJNDI(config: Configuration, hikariConfig: HikariConfig, dataSource: DataSource): Unit = {
    config.getString("jndiName") map { name =>
      JNDI.initialContext.rebind(name, dataSource)
      Logger.info(s"""datasource [${hikariConfig.getJdbcUrl}] bound to JNDI as $name""")
    }
  }
}
