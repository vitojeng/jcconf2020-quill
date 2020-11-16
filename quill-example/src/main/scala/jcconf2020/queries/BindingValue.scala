package jcconf2020.queries

import io.getquill.{LowerCase, PostgresJdbcContext}
import pprint._

object BindingValueSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def queryCountryCities(countryCode: String) = quote {
    /*
     *  Compile error:
     *    Found the following free variables: countryCode.
     *    Quotations can't reference values outside their scope directly.
     *    In order to bind runtime values to a quotation, please use the method `lift`.
     */
    //query[City].filter(c=>c.countryCode==countryCode)
    query[City].filter(c=>c.countryCode==lift(countryCode))
  }

  def queryCountryCities(countries: Seq[String]) = quote {
    query[City].filter(c=> liftQuery(countries).contains(c.countryCode))
  }

  def insertCities(cities: Seq[City]) = quote {
    liftQuery(cities).foreach { c =>
      query[City].insert(c)
    }
  }

  def deleteCities(cities: Seq[City]) = quote {
    liftQuery(cities).foreach { c0 =>
      query[City].filter(c1=>c0.id==c1.id).delete
    }
  }

  def main(args: Array[String]): Unit = {
    val result1 = ctx.run(queryCountryCities("TWN"))
    result1.foreach(pprintln(_))

    val result2 = ctx.run(queryCountryCities("JPN"))
    result2.foreach(pprintln(_))

    val result3 = ctx.run(queryCountryCities(Seq("TWN", "JPN")))
    result3.foreach(pprintln(_))

    val cities = Seq(
      City(8001, "Scala", "TWN", "Taipei", 2000),
      City(8002, "Java", "TWN", "Hsinchu", 5000),
      City(8003, "Go", "TWN", "Kaohsiung", 1800)
    )
    ctx.run(deleteCities(cities))
    ctx.run(insertCities(cities))
  }

}