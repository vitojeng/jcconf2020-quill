package jcconf2020.quill.async

import io.getquill._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

object AsyncSample {
  val ctx = new PostgresJAsyncContext(LowerCase, "jasyncCtx")
  import ctx._

  def main(args: Array[String]): Unit = {
    val queryCities = quote {
      query[City]
              .filter(c=> c.countryCode=="USA" && c.population > 1000000)
              .map(c=> (c.id, c.name, c.population) )
    }

    val result = ctx.run(queryCities)
    val r: Seq[(Index, String, Index)] = Await.result(result, Duration.Inf)

    r.foreach(println(_))

    ctx.close()
  }

}
