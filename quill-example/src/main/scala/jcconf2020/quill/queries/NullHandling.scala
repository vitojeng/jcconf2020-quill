package jcconf2020.quill.queries

import com.github.vertical_blank.sqlformatter.SqlFormatter
import io.getquill._
import pprint._

object NullHandlingSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  // Option.isEmpty
  def sample_isDefined(): Unit = {
    // SELECT c.code, c.name, c.continent, c.region, c.surfacearea, c.indepyear, c.population, c.lifeexpectancy, c.gnp, c.gnpold, c.localname, c.governmentform, c.headofstate, c.capital, c.code2
    //   FROM country c
    //   WHERE c.indepyear IS NOT NULL
    val q = quote {
      query[Country].filter(c=>c.indepYear.isDefined)
    }
    println( SqlFormatter.format( ctx.translate(q) ))
    val result = ctx.run(q)
    result.foreach(pprintln(_))
  }

  // Option.isEmpty
  def sample_isEmpty(): Unit = {
    // SELECT c.code, c.name, c.continent, c.region, c.surfacearea, c.indepyear, c.population, c.lifeexpectancy, c.gnp, c.gnpold, c.localname, c.governmentform, c.headofstate, c.capital, c.code2
    //   FROM country c
    //   WHERE c.indepyear IS NOT NULL
    val q = quote {
      query[Country].filter(c=>c.indepYear.isEmpty)
    }
    println( SqlFormatter.format( ctx.translate(q) ))
    val result = ctx.run(q)
    result.foreach(pprintln(_))
  }

  // Option.map
  def sample_map(): Unit = {
    // SELECT c.name, 'Independent: ' || c.indepyear
    //   FROM country c
    val q = quote {
      query[Country]
              .map{c=> (c.name, c.indepYear.map(y=>s"Independent: $y")) }
    }
    println( SqlFormatter.format( ctx.translate(q) ))
    val result = ctx.run(q)
    result.foreach(pprintln(_))
  }

  def main(args: Array[String]): Unit = {
    sample_isDefined()
    sample_isEmpty()
    sample_map()
  }

}