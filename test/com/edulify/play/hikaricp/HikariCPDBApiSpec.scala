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

import java.sql.DriverManager
import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.jdbcdslog.ConnectionPoolDataSourceProxy
import org.specs2.execute.AsResult
import org.specs2.mutable.Specification
import org.specs2.specification.{Scope, AroundExample}
import play.api.Configuration
import play.api.libs.JNDI
import scala.collection.JavaConversions._

class HikariCPDBApiSpec extends Specification with AroundExample {

  "When starting HikariCP DB API" should {
    "create data sources" in new DataSourceConfigs {
      val api = new HikariCPDBApi(config, classLoader)
      val ds = api.getDataSource("default")
      ds.getConnection.getMetaData.getURL == "jdbc:h2:mem:test"
    }
    "create data source with logSql enabled" in new DataSourceConfigs {
      val api = new HikariCPDBApi(configWithLogSql, classLoader)
      val ds = api.getDataSource("default")
      ds.isInstanceOf[ConnectionPoolDataSourceProxy] must beTrue
    }
    "bind data source to jndi" in new DataSourceConfigs {
      val api = new HikariCPDBApi(configWithLogSql, classLoader)
      val ds = api.getDataSource("default")
      JNDI.initialContext.lookup("TestContext") != null
    }
    "register driver configured in `driverClassName`" in new DataSourceConfigs {
      val api = new HikariCPDBApi(configWithLogSql, classLoader)
      val ds = api.getDataSource("default")
      DriverManager.getDrivers.exists( driver => driver.getClass.getName == "org.h2.Driver") must beTrue
    }
    "create more than one datasource" in new DataSourceConfigs {
      val api = new HikariCPDBApi(multipleDataSources, classLoader)
      api.getDataSource("default")  != null
      api.getDataSource("default2") != null
    }
  }

  def around[T : AsResult](t: =>T) = {
    Class.forName("org.h2.Driver")
    val conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa",  "")
    try {
      val result = AsResult(t)
      result
    } catch {
      case e: Exception => failure(e.getMessage)
    } finally {
      conn.close()
    }
  }
}

trait DataSourceConfigs extends Scope {
  def config = new Configuration(ConfigFactory.parseProperties(Props().properties))
  def configWithLogSql = {
    val props = new Props().properties
    props.setProperty("default.logSql", "true")
    new Configuration(ConfigFactory.parseProperties(props))
  }
  def multipleDataSources = new Configuration(ConfigFactory.parseProperties(Props().multipleDatabases))
  def classLoader = this.getClass.getClassLoader
}

case class Props() {
  def properties = {
    val props = new Properties()
    props.setProperty("default.jdbcUrl", "jdbc:h2:mem:test")
    props.setProperty("default.username", "sa")
    props.setProperty("default.password", "")
    props.setProperty("default.driverClassName", "org.h2.Driver")
    props.setProperty("default.jndiName", "TestContext")
    props
  }
  def multipleDatabases = {
    val props = new Properties()
    props.setProperty("default.jdbcUrl", "jdbc:h2:mem:test")
    props.setProperty("default.username", "sa")
    props.setProperty("default.password", "")
    props.setProperty("default.driverClassName", "org.h2.Driver")
    props.setProperty("default.jndiName", "TestContext")

    // default2
    props.setProperty("default2.jdbcUrl", "jdbc:h2:mem:test")
    props.setProperty("default2.username", "sa")
    props.setProperty("default2.password", "")
    props.setProperty("default2.driverClassName", "org.h2.Driver")
    props.setProperty("default2.jndiName", "TestContext")
    props
  }
}