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

import java.sql.{SQLException, DriverManager}
import java.util.Properties

import com.typesafe.config.ConfigFactory
import play.api.{PlayException, Configuration, Mode}

import org.specs2.mock._
import org.specs2.execute.AsResult
import org.specs2.mutable.Specification
import org.specs2.specification.{Scope, AroundExample}

class HikariCPPluginSpec extends Specification with AroundExample {

  "When configuring the plugin" should {
    "check that plugin is enabled" in new MockedApplication {
      val plugin = new HikariCPPlugin(app())
      plugin.enabled must beTrue
    }
    "check that plugin is disabled" in new MockedApplication {
      val plugin = new HikariCPPlugin(appWithHikariDisabled())
      plugin.enabled must beFalse
    }
  }

  "When starting the plugin" should {
    "check that pool is working" in new MockedApplication {
      val plugin = new HikariCPPlugin(app())
      plugin.onStart() must not(throwA[PlayException])
    }
    "check that pool is not working" in new MockedApplication {
      val plugin = new HikariCPPlugin(appWithMisconfiguredPool())
      plugin.onStart() must throwA[PlayException]
    }
  }

  "When stopping the plugin" should {
    "stop the configured data sources" in new MockedApplication {
      val plugin = new HikariCPPlugin(app())
      plugin.onStart()
      plugin.onStop()
      val ds = plugin.api.getDataSource("default")
      ds.getConnection must throwA[SQLException]
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

trait MockedApplication extends Scope with Mockito {
  def appWithTestMode() = {
    val app = mock[play.api.Application]
    app.mode returns Mode.Test
    app.configuration returns new Configuration(ConfigFactory.parseProperties(properties()))
    app
  }

  def app() = {
    val app = mock[play.api.Application]
    app.mode returns Mode.Dev
    app.configuration returns new Configuration(ConfigFactory.parseProperties(properties()))
    app
  }

  def appWithHikariDisabled() = {
    val props = properties()
    props.setProperty("hikari.enabled", "false")

    val app = mock[play.api.Application]
    app.mode returns Mode.Dev
    app.configuration returns new Configuration(ConfigFactory.parseProperties(props))
    app
  }

  def appWithMisconfiguredPool() = {
    val props = properties()
    props.setProperty("db.default.jdbcUrl", "") // misconfigured

    val app = mock[play.api.Application]
    app.mode returns Mode.Dev
    app.configuration returns new Configuration(ConfigFactory.parseProperties(props))
    app
  }

  private def properties() = {
    val props = new Properties()
    props.setProperty("db.default.jdbcUrl", "jdbc:h2:mem:test")
    props.setProperty("db.default.username", "sa")
    props.setProperty("db.default.password", "")
    props.setProperty("db.default.jndiName", "TestContext")
    props
  }
}
