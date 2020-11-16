package jcconf2020.demo

import io.getquill._
import pprint._

object CountryCitiesSample1 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val q = quote {
      for {
        country <- query[Country].filter(c=>c.code=="TWN")
        city <- query[City].join(ci=>ci.countryCode==country.code)
      } yield {
        (country.name, city.name)
      }
    }
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}

object CountryCitiesSample2 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCounrtyCities = quote {
      (queryCountry: Query[Country]) =>
        for {
          country <- queryCountry
          city <- query[City].join(ci=>ci.countryCode==country.code)
        } yield {
          (country.name, city.name)
        }
    }

    val queryTaiwan = quote(query[Country].filter(c=>c.code=="TWN"))
    val q = quote {
      queryCounrtyCities( queryTaiwan )
    }
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}

object CountryCitiesSample3 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCounrtyCities = quote {
      (queryCountry: Query[Country]) =>
        for {
          country <- queryCountry
          city <- query[City].join(ci=>ci.countryCode==country.code)
        } yield {
          (country.name, city.name)
        }
    }

    val queryCountry = quote {
      (countryCode: String) =>
        query[Country].filter(c=>c.code==countryCode)
    }

    val TaiwanCode = "TWN"
    val queryTaiwan = quote( queryCountry(lift(TaiwanCode)) )

    val q = quote( queryCounrtyCities(queryTaiwan) )
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}