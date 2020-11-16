package jcconf2020.queries

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


//trait MyPostgresContext {
//
//  //val ctx = new PostgresJdbcContext(LowerCase, dataSource)
//  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
//
//  private def dataSource = {
//    val pgDataSource = new PGSimpleDataSource()
//    pgDataSource.setUser("postgres")
//    pgDataSource.setDatabaseName("postgres")
//
//    val config = new HikariConfig()
//    config.setDataSource(pgDataSource)
//    new HikariDataSource(config)
//  }
//
//}