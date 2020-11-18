package jcconf2020.quill.queries

import io.getquill._
import pprint._


object QuerySample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCity = quote {
      query[Country]
              .filter(c=>c.population>=100000000)
              .flatMap { co =>
                query[City]
                        .filter(ci => ci.countryCode == co.code && ci.population>=2000000)
                        .map(ci=>(co.name, ci))
              }
    }
    val result = ctx.run(queryCity)
    result.foreach(pprintln(_))
  }

}

object SortBySample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCountry = quote {
      query[Country]
              .filter(co=> liftQuery(Set("Asia", "Europe")).contains(co.continent) )
              .sortBy(co=>co.population)(Ord.desc)
    }
    ctx.run(queryCountry)
  }
}

object GroupBySample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCityCount = quote {
      query[City]
              .groupBy(ci=>ci.countryCode)
              .map { case (country, city) => (country, city.size) }
              .sortBy(r=>r._1)
    }
    val result = ctx.run(queryCityCount)
    result.foreach(pprintln(_))
  }

}

object DistinctSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCountry = quote {
      query[Country]
              .map(co=>(co.code, co.name))
              .distinct
              .sortBy(co=>co._1)
    }
    val result = ctx.run(queryCountry)
    result.foreach(pprintln(_))
  }

}
