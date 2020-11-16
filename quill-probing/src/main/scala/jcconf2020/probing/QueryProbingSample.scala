package jcconf2020.probing

import jcconf2020.context.MyQuillContext
import pprint.pprintln

case class City(id: Int,
        name: String,
        countryCode: String,
        district: String,
        population: Int)

object QueryProbingSample {

  val ctx = MyQuillContext
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCity = quote {
      query[City].filter(c=>c.population >= 100000000)
    }
    val result = ctx.run(queryCity)
    result.foreach(pprintln(_))
  }

}
