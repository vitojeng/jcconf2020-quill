package jcconf2020.context

import io.getquill._

object MyQuillContext extends
        PostgresJdbcContext(LowerCase, "ctx") with QueryProbing
