package jcconf2020.async

import java.util.concurrent.TimeUnit

import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.pool.{ConnectionPool, PoolConfiguration}
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import com.github.jasync.sql.db.postgresql.{PostgreSQLConnection, PostgreSQLConnectionBuilder}
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.{LowerCase, PostgresJAsyncContext, PostgresJAsyncContextConfig, PostgresJdbcContext}
import org.postgresql.ds.PGSimpleDataSource

/*
  Working with Databases using Scala and Quill
  https://www.lihaoyi.com/post/WorkingwithDatabasesusingScalaandQuill.html

  PostgreSql DbSamples:
  https://www.postgresql.org/ftp/projects/pgFoundry/dbsamples/

 */

case class City(id: Int,
        name: String,
        countryCode: String,
        district: String,
        population: Int)

case class Country(code: String,
        name: String,
        continent: String,
        region: String,
        surfaceArea: Double,
        indepYear: Option[Int],
        population: Int,
        lifeExpectancy: Option[Double],
        gnp: Option[scala.math.BigDecimal],
        gnpold: Option[scala.math.BigDecimal],
        localName: String,
        governmentForm: String,
        headOfState: Option[String],
        capital: Option[Int],
        code2: String)

case class CountryLanguage(countrycode: String,
        language: String,
        isOfficial: Boolean,
        percentage: Double)

trait MyPostgresContext {

}