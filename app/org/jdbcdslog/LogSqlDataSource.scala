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
package org.jdbcdslog

import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger

import com.zaxxer.hikari.HikariDataSource

/**
 * This class is necessary because jdbcdslog proxies does not
 * exposes the target dataSource, which is necessary to shutdown
 * the pool.
 */
class LogSqlDataSource extends ConnectionPoolDataSourceProxy {
  override def getParentLogger: Logger = throw new SQLFeatureNotSupportedException

  def shutdown() = this.targetDS match {
    case ds: HikariDataSource => ds.close()
    case _ => play.api.Logger.info("Not a HikariDataSource, so it will not shutdown the pool")
  }
}
