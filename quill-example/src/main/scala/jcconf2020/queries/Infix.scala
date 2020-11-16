package jcconf2020.queries

import io.getquill._
import pprint._

object RawSqlQuerySample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  // Mapping to case class
  def sample_mapping_caseclass(): Unit = {
    val TaiwanCode = "TWN"
    val queryCities = quote {
      (countryCode: String) =>
        infix"select * from city where countrycode=$countryCode".as[Query[City]]
    }
    val result = ctx.run(queryCities(lift(TaiwanCode)))
    result.foreach(pprintln(_))

    // Create case class for the query result
    case class CityName(id: Int, name: String)
    val queryCities2 = quote {
      infix"select id, name from city where countrycode='TWN'".as[Query[CityName]]
    }
    ctx.run(queryCities2).foreach(pprintln(_))
  }

  // Mapping to TupleX
  def sample_mapping_tuplex(): Unit = {
    // ERROR: column x._1 does not exist
/*
    val queryCities1 = quote {
      infix"select id, name from city where countrycode='TWN'".as[Query[(Int, String)]]
    }
    ctx.run(queryCities1).foreach(pprintln(_))
*/

    // Specify column name: _1, _2
    val queryCities2 = quote {
      infix"select id as _1, name as _2 from city where countrycode='TWN'".as[Query[(Int, String)]]
    }
    ctx.run(queryCities2).foreach(pprintln(_))
  }

  def main(args: Array[String]): Unit = {
    sample_mapping_caseclass()
    sample_mapping_tuplex()
  }

}

object InfixWithImplicitClass {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  implicit class ForUpdate[T](q: Query[T]) {
    def forUpdate = quote {
      infix"$q FOR UPDATE".as[Query[T]]
    }
  }

  def sample_forUpdate(): Unit = {
    val queryCity = quote {
      query[City].filter(c=>c.countryCode=="TWN").forUpdate
    }
    ctx.run(queryCity).foreach(pprintln(_))
  }

  def main(args: Array[String]): Unit = {
    sample_forUpdate()
  }

}

object InfixWithMap {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    sample_map_1()
    sample_map_2()
  }

  def sample_map_1(): Unit = {
    val queryCity = quote {
      query[Country].map { c =>
        (c.code,
                infix"UPPER(${c.name})".as[String],
                infix"POSITION('America' in ${c.continent})>0".as[Boolean],
        )
      }
    }
    ctx.run(queryCity).foreach(pprintln(_))
  }

  def sample_map_2(): Unit = {
    val upperCase = quote {
      (str: String) => infix"UPPER($str)".as[String]
    }
    val isAmerica = quote {
      (continent: String) => infix"POSITION('America' in $continent)>0".as[Boolean]
    }

    val queryCity = quote {
      query[Country].map { c =>
        (c.code, upperCase(c.name), isAmerica(c.continent))
      }
    }
    ctx.run(queryCity).foreach(pprintln(_))
  }

}