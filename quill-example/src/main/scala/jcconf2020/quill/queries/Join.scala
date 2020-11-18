package jcconf2020.quill.queries

import io.getquill._
import pprint._

object ApplicativeJoinSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  /*
     SELECT x11.name, x01.name FROM city x01
        INNER JOIN country x11 ON x01.countrycode = x11.code
        WHERE x11.continent = 'Asia'
   */
  val queryAsiaCities = quote {
    query[City]
            .join(query[Country])
            .on { (ci, co) => ci.countryCode == co.code }
            .filter { case (_, co)=> co.continent=="Asia" }
            .map { case (ci, co) => (co.name, ci.name) }
  }

  val queryTaiwanCities = quote {
    query[City]
            .leftJoin(query[Country])
            .on { (ci, co) => ci.countryCode == co.code }
            .filter { case (ci, _)=> ci.countryCode=="TWN" }
            .map { case (ci, co) => (ci.name, ci.district, co.map(c=>c.name)) }
  }

  val taiwanCities2 = quote {
    query[City]
            .join(query[Country]).on { case (ci, co)=> ci.countryCode==co.code }
            .join(query[CountryLanguage]).on { case ((_, co), cl)=> co.code==cl.countrycode }
            .filter { case ((_, co), _)=> co.code=="TWN" }
            .map { case ((ci, co), cl)=> (ci.name, co.name, cl.language) }
  }

  def main(args: Array[String]): Unit = {
    println(ctx.run(queryAsiaCities))
    println(ctx.run(queryTaiwanCities))
    println(ctx.run(taiwanCities2))
  }
}


object ImplicitJoinSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  val queryTaiwanCities = quote {
    for {
      ci <- query[City]
      co <- query[Country].filter(co0=>co0.code=="TWN")
              if (ci.countryCode==co.code)
      cl <- query[CountryLanguage]
              if (co.code==cl.countrycode)
    } yield (ci.name, co.name, cl.language)
  }

  def main(args: Array[String]): Unit = {
    ctx.run(queryTaiwanCities).foreach(println(_))
  }

}


object FlatJoinSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  val queryTaiwanCities = quote {
    for {
      co <- query[Country].filter(c=>c.code=="TWN")
      ci <- query[City]
              .leftJoin(co1=>co.code==co1.countryCode)
              .filter(ci1=>ci1.exists(c=>c.population>1000000))
    } yield {
      (co, ci)
    }
  }

  def main(args: Array[String]): Unit = {
    //SqlFormatter.println(ctx.translate(flatJoin))

    val result = ctx.run(queryTaiwanCities)
    result.foreach(pprintln(_))
  }

}
