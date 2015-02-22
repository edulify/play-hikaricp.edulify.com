package org.jdbcdslog

import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger

import com.zaxxer.hikari.HikariDataSource

class LogSqlDataSource extends ConnectionPoolDataSourceProxy {
  override def getParentLogger: Logger = throw new SQLFeatureNotSupportedException

  def shutdown() = this.targetDS match {
    case ds: HikariDataSource => ds.shutdown()
    case _ => play.api.Logger.info("Not a HikariDataSource, so it will not shutdown the pool")
  }
}
