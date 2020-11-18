package jcconf2020.quill.queries

import com.github.vertical_blank.sqlformatter.SqlFormatter
import io.getquill._
import pprint._

case class Person(id:Int, name:String, age:Int)

object DynamicQuotation {
  val ctx = new SqlMirrorContext(MirrorSqlDialect, Literal)

  import ctx._

  sealed trait QueryType
  case object Minor extends QueryType
  case object Senior extends QueryType

  def people(t: QueryType) =
    t match {
      case Minor => quote {
        query[Person].filter(p => p.age < 18)
      }
      case Senior => quote {
        query[Person].filter(p => p.age > 65)
      }
    }

  def main(args: Array[String]): Unit = {
    ctx.run(people(Minor))
    ctx.run(people(Senior))
  }

}

object DynamicQuerySample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def sample_runtime(): Unit = {
    val query1 = quote {
      query[City].filter(c=>c.countryCode=="TWN")
    }
    ctx.run(query1).foreach(pprintln(_))

    val query2: Quoted[EntityQuery[City]] = quote {
      query[City].filter(c=>c.countryCode=="TWN")
    }   // Dynamic
    println(SqlFormatter.format( ctx.translate(query2) ))
    ctx.run(query2).foreach(pprintln(_))
  }

  def sample_dynamic(): Unit = {
    val queryCity = quote {
      query[City]
    }
    val q = queryCity.dynamic.filter { c: Quoted[City] =>
      c.countryCode == "TWN"
    }
    println(SqlFormatter.format( ctx.translate(q) ))
    ctx.run(q).foreach(pprintln(_))
  }

  def sample_dynamicQuery(): Unit = {
    val q = dynamicQuery[City].filter { c: Quoted[City] =>
      c.countryCode == "TWN"
    }
    println(SqlFormatter.format( ctx.translate(q) ))
    ctx.run(q).foreach(pprintln(_))
  }

  case class MyCity(id: Int, city_name: String,
          country_code: String, district: String,
          population: Int)

  def sample_dynamicQuerySchema(): Unit = {
    val querySchemaCity = dynamicQuerySchema[MyCity]("city",
      alias(_.city_name, "name"),
      alias(_.country_code, "countrycode")
    )
    val query1 = querySchemaCity.filter(c=>quote(c.country_code=="TWN"))

    println(SqlFormatter.format( ctx.translate(query1) ))
    ctx.run(query1).foreach(pprintln(_))
  }

  def main(args: Array[String]): Unit = {
    sample_runtime()
    sample_dynamic()
    sample_dynamicQuery()
    sample_dynamicQuerySchema()
  }

}
