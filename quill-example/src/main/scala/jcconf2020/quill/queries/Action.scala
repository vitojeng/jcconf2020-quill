package jcconf2020.quill.queries

import io.getquill.{LowerCase, PostgresJdbcContext}

object ActionBasicSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val deleteCity = quote {
      query[City].filter(c => c.id==10000).delete
    }
    val insertCity = quote {
      query[City].insert(City(10000, "my city", "MYC", "My District", 0))
    }
    val updateDistrict = quote {
      query[City].filter(_.district == "My District").update(_.district -> "My Town")
    }

    println("delete: " + ctx.run(deleteCity))
    println("insert: " + ctx.run(insertCity))
    println("update: " + ctx.run(updateDistrict))
    println("delete again: " + ctx.run(deleteCity))
  }
}

object BatchActionSample {

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

    val deleteCities = quote {
      liftQuery(List(10001, 10002, 10003))
              .foreach(id => query[City].filter(c=>c.id==id).delete)
    }

    println("batch delete: " + ctx.run(deleteCities))
    println("batch insert: " + ctx.run(insertCities))
    println("batch delete: " + ctx.run(deleteCities))
  }
}
