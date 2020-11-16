package jcconf2020.demo

import io.getquill._
import pprint._

object FourAsianTigerCities extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  val TaiwanCode = "TWN"
  val HongKongCode = "HKG"
  val SouthKoreaCode = "KOR"
  val SingaporeCode = "SGP"

  val queryCountry = quote {
    (countryCode: String) =>
      query[Country].filter(c=>c.code==countryCode)
  }

  val queryTaiwan = quote( queryCountry(lift(TaiwanCode)) )
  val queryHongKong = quote( queryCountry(lift(HongKongCode)) )
  val querySouthKorea = quote( queryCountry(lift(SouthKoreaCode)) )
  val querySingapore = quote( queryCountry(lift(SingaporeCode)) )

  val queryCounrtyCities = quote {
    (queryCountry: Query[Country]) =>
      for {
        country <- queryCountry
        city <- query[City].join(ci=>ci.countryCode==country.code)
      } yield {
        (country.name, city.name)
      }
  }

  def main(args: Array[String]): Unit = {
    val queryFourAsianTigers = quote {
      queryTaiwan ++ queryHongKong ++ querySouthKorea ++ querySingapore
    }

    val q = quote( queryCounrtyCities(queryFourAsianTigers) )
    val result = ctx.run(q)
    printlnSql( ctx.translate(q) )
    pprintln( result, height = 15 )
    println( s"Total: ${result.size}" )

  }

}


object FourAsianTigerCities2 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  val TaiwanCode = "TWN"
  val HongKongCode = "HKG"
  val SouthKoreaCode = "KOR"
  val SingaporeCode = "SGP"
  val fourAsianTigerCodes = Seq(TaiwanCode, HongKongCode, SouthKoreaCode, SingaporeCode)

  val queryCountries = quote {
    (codes: Query[String]) =>
      query[Country].filter(c=>codes.contains(c.code))
  }

  val queryCounrtyCities = quote {
    (queryCountry: Query[Country]) =>
      for {
        country <- queryCountry
        city <- query[City].join(ci=>ci.countryCode==country.code)
      } yield {
        (country.name, city.name)
      }
  }

  // Use def to declare method
/*
  def queryCountries2(codes: Seq[String]) = quote {
    query[Country].filter(c=>liftQuery(codes).contains(c.code))
  }

  def queryCounrtyCities2(queryCountry: Query[Country]) = quote {
    for {
      country <- queryCountry
      city <- query[City].join(ci=>ci.countryCode==country.code)
    } yield {
      (country.name, city.name)
    }
  }
*/

  def main(args: Array[String]): Unit = {
    val queryFourAsianTigers = quote {
      queryCountries( liftQuery(fourAsianTigerCodes) )
    }
    val q = quote( queryCounrtyCities(queryFourAsianTigers) )
    val result = ctx.run(q)
    printlnSql( ctx.translate(q) )
    pprintln( result, height = 15 )
    println( s"Total: ${result.size}" )
  }

}