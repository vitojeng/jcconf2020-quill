package jcconf2020.quill.queries

import com.github.vertical_blank.sqlformatter.SqlFormatter
import io.getquill._
import pprint._

object NullHandlingSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

//  val schemaCountry = quote {
//    querySchema[Country2]("country")
//  }

  val schemaCountry = quote { query[Country] }

  // Option.isEmpty
  def sample_isDefined(): Unit = {
    // SELECT c.code, c.name, c.continent, c.indepyear, c.population, c.headofstate, c.capital
    //   FROM country c
    //   WHERE c.indepyear IS NOT NULL
    val queryCountry = quote {
      schemaCountry.filter(c=>c.indepYear.isDefined)
    }
    println( SqlFormatter.format( ctx.translate(queryCountry) ))
    val result = ctx.run(queryCountry)
    result.foreach(pprintln(_))
  }

  // Option.isEmpty
  def sample_isEmpty(): Unit = {
    // SELECT c.code, c.name, c.continent, c.indepyear, c.population, c.headofstate, c.capital
    //   FROM country c
    //   WHERE c.indepyear IS NULL
    val queryCountry = quote {
      schemaCountry.filter(c=>c.indepYear.isEmpty)
    }
    println( SqlFormatter.format( ctx.translate(queryCountry) ))
    val result = ctx.run(queryCountry)
    result.foreach(pprintln(_))
  }

  // Option.forall
  def sample_forall(): Unit = {
    // SELECT c.code, c.name, c.continent, c.indepyear, c.population, c.headofstate, c.capital
    //   FROM country c
    //   WHERE c.indepyear IS NULL OR c.indepyear = 1956
    val queryCountry = quote {
      schemaCountry.filter(c=>c.indepYear.forall(y=>y==1956))
    }
    println( SqlFormatter.format( ctx.translate(queryCountry) ))
    val result = ctx.run(queryCountry)
    result.foreach(pprintln(_))
  }

  // Option.map
  def sample_map(): Unit = {
    // SELECT c.name, 'Independent: ' || c.indepyear
    //   FROM country c
    //   WHERE c.indepyear IS NOT NULL
    val queryCountry = quote {
      schemaCountry.filter(c=>c.indepYear.isDefined)
              .map{c=> (c.name, c.indepYear.map(y=>s"Independent: $y")) }
    }
    println( SqlFormatter.format( ctx.translate(queryCountry) ))
    val result = ctx.run(queryCountry)
    result.foreach(pprintln(_))
  }

  // Option equals
  def sample_getOrNull(): Unit = {
    val query1 = quote {
      schemaCountry.filter(c=>c.indepYear == Option(1956) )
    }
    println( SqlFormatter.format( ctx.translate(query1) ))

    val query2 = quote {
      schemaCountry.filter(c=>c.indepYear == None )
    }

    println( SqlFormatter.format( ctx.translate(query2) ))
  }

  def main(args: Array[String]): Unit = {
    sample_isDefined()
//    sample_isEmpty()
//    sample_forall()
//    sample_map()
//    sample_getOrNull()
  }

}