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

import play.api.db.{DBApi, DBPlugin}
import play.api.{Application, Configuration, Logger}

import scala.util.{Success, Failure, Try}

class HikariCPPlugin(app: Application) extends DBPlugin {

  lazy val databaseConfig = app.configuration.getConfig("db").getOrElse(Configuration.empty)

  override def enabled = app.configuration.getBoolean("hikari.enabled").getOrElse(true)

  private val hikariCPDBApi: DBApi = new HikariCPDBApi(databaseConfig, app.classloader)

  def api: DBApi = hikariCPDBApi

  override def onStart() = Logger.info("Starting HikariCP connection pool...")

  override def onStop() {
    Logger.info("Stopping HikariCP connection pool...")
    hikariCPDBApi.datasources.foreach {
      case (ds, name) => Try {
        hikariCPDBApi.shutdownPool(ds)
      } match {
        case Success(r) => Logger.info(s"HikariCP connection pool [$name] was terminated")
        case Failure(t) => Logger.error(s"Was not able to shutdown the connection pool [$name]", t)
      }
    }
  }
}
