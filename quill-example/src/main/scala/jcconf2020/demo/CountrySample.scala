package jcconf2020.demo

import io.getquill._
import pprint._

object CountrySample1 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val q = quote {
      query[Country].filter(c=>c.code=="TWN")
    }
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}


object CountrySample2 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCountry = quote {
      (countryCode: String) =>
        query[Country].filter(c=>c.code==countryCode)
    }
    val q = quote( queryCountry("TWN") )
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}


object CountrySample3 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCountry = quote {
      (f: Country => Boolean) =>
        query[Country].filter(f(_))
    }
    val q = quote( queryCountry(_.code=="TWN") )
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}

object CountrySample4 extends ConsolePrint {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCountry = quote { (f: Country => Boolean) =>
        query[Country].filter(f(_))
    }

    val TaiwanCode = "TWN"
    val HongKongCode = "HKG"
    val queryTaiwan = quote( queryCountry(_.code==lift(TaiwanCode)) )
    val queryHongKong = quote( queryCountry(_.code==lift(HongKongCode)) )

    val q = quote( queryTaiwan ++ queryHongKong )
    printlnSql( ctx.translate(q) )
    pprintln( ctx.run(q), height = 15 )
  }

}
