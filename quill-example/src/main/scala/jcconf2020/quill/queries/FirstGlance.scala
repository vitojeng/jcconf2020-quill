package jcconf2020.quill.queries

import io.getquill._
import pprint._

object FirstGlance {

  val ctx = new SqlMirrorContext(PostgresDialect, LowerCase)
  import ctx._

  def main(args: Array[String]): Unit = {
    val q = quote {
      query[City]
          .filter(c=> c.countryCode=="USA" && c.population > 1000000)
          .map(c=> (c.id, c.name, c.population) )
    }
    pprintln(q.ast)
    val result = ctx.run(q)
    pprintln(result.ast)
  }
}


object FirstGlance_Postgres {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val q = quote {
      query[City].filter(c=> c.countryCode=="USA" && c.population > 1000000)
              .map(c=> (c.id, c.name, c.population) )
    }
    pprintln(q.ast)
    pprintln(q.toString)
    val result = ctx.run(q)
    ctx.close()
  }

}

