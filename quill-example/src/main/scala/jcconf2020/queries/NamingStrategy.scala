package jcconf2020.queries

import io.getquill._

object NamingStrategy {

  def main(args: Array[String]): Unit = {
    val namingStrategies = Seq(LowerCase, UpperCase, SnakeCase,
      CamelCase, Literal, Escape)
    val words = Seq("countrycode", "countryCode", "country_code")
    namingStrategies.foreach { naming: NamingStrategy =>
      val result = words.map { w =>
        val table = naming.table(w)
        val column = naming.column(w)
        s"$w => $table,$column"
      }
      println(s"\n${naming.getClass.getSimpleName}:")
      println("  " + result.mkString("\n  "))
    }

  }
}