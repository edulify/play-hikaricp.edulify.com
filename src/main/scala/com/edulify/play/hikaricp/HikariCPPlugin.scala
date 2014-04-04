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

import play.api.{Application, Configuration}
import play.api.db.{DBApi, DBPlugin}

class HikariCPPlugin(app: Application) extends DBPlugin {

  lazy val databaseConfig = app.configuration.getConfig("db").getOrElse(Configuration.empty)

  override def enabled = true

  private lazy val hirakiCPDBApi: DBApi = new HirakiCPDBApi(databaseConfig)

  def api: DBApi = hirakiCPDBApi

  override def onStart() {
    play.api.Logger.info("Starting HikariCP connection pool...")
    api.datasources.map { ds =>
      try {
        ds._1.getConnection.close()
      } catch {
        case t: Throwable =>
          throw databaseConfig.reportError(ds._2 + ".url", "Cannot connect to database [" + ds._2 + "]", Some(t.getCause))
      }
    }
  }

  override def onStop() {
    play.api.Logger.info("Stoping HikariCP connection pool...")
    api.datasources.foreach {
      case (ds, _) => try {
        api.shutdownPool(ds)
      } catch { case t: Throwable => }
    }
  }
}