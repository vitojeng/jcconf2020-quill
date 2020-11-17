package jcconf2020.quill.queries

import io.getquill._

object CustomSchemaSample {

  val ctx = new PostgresJdbcContext(LowerCase, "ctx")
  import ctx._

  case class MyCity(id: Int,
          city_name: String,
          country_code: String,
          district: String,
          population: Int)

  def main(args: Array[String]): Unit = {
    val schemaCity = quote {
      querySchema[MyCity]("city",
        _.country_code -> "countrycode",
        _.city_name -> "name"
      )
    }
    val queryCities = quote {
      schemaCity.filter(c=>c.country_code=="TWN")
    }
    val cities = ctx.run(queryCities)
    cities.foreach(println)
  }

}

