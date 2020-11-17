package jcconf2020.quill.queries

import io.getquill._

object TransactionSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val cities = List(
      City(10001, "my city1", "MYC", "My Town", 100000),
      City(10002, "my city2", "MYC", "My Village", 120000),
      City(10003, "my city3", "MYC", "My Borough", 140000)
    )
    val insertCities = quote {
      liftQuery(cities).foreach(e=>query[City].insert(e))
    }
    ctx.transaction {
      ctx.run(insertCities)
      throw new Exception("transaction failed!")
    }
  }

}
