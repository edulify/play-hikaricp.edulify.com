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

import java.sql.Connection

import play.api.db.{DBApi, DBPlugin}
import play.api.{Application, Configuration, Mode, Logger}

import scala.util.control.NonFatal

class HikariCPPlugin(app: Application) extends DBPlugin {

  lazy val databaseConfig = app.configuration.getConfig("db").getOrElse(Configuration.empty)

  override def enabled = app.configuration.getBoolean("hikari.enabled").getOrElse(true)

  private lazy val hikariCPDBApi: DBApi = new HikariCPDBApi(databaseConfig, app.classloader)

  def api: DBApi = hikariCPDBApi

  override def onStart() {
    play.api.Logger.info("Starting HikariCP connection pool...")
    hikariCPDBApi.datasources.map { ds =>
        try {
          ds._1.getConnection.close()
          app.mode match {
            case Mode.Test =>
            case mode => Logger.info(s"database [$ds._2] connected at ${dbURL(ds._1.getConnection)}")
          }
        } catch {
          case NonFatal(e) =>
            throw databaseConfig.reportError(s"$ds._2.url", s"Cannot connect to database [$ds._2]", Some(e.getCause))
        }
    }
  }

  override def onStop() {
    play.api.Logger.info("Stopping HikariCP connection pool...")
    hikariCPDBApi.datasources.foreach {
      case (ds, _) => try {
        hikariCPDBApi.shutdownPool(ds)
      } catch {
        case t: Throwable =>
      }
    }
  }

  private def dbURL(conn: Connection): String = {
    val u = conn.getMetaData.getURL
    conn.close()
    u
  }
}
