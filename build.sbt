
ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "example.quill"
ThisBuild / organizationName := "example"

val verQuill = "3.5.2"
val verPostgresql = "42.2.14"

lazy val example = (project in file("quill-example"))
        .settings(
          name := "example",
          libraryDependencies += "io.getquill" %% "quill-jdbc" % verQuill,
          libraryDependencies += "io.getquill" %% "quill-jasync-postgres" % verQuill,
          libraryDependencies += "org.postgresql" % "postgresql" % verPostgresql,
          libraryDependencies += "com.lihaoyi" %% "pprint" % "0.5.6"
        )

lazy val context = (project in file("quill-context"))
        .settings(
          name := "context",
          libraryDependencies += "io.getquill" %% "quill-jdbc" % verQuill,
          libraryDependencies += "org.postgresql" % "postgresql" % verPostgresql,
        )

lazy val probing = (project in file("quill-probing"))
        .dependsOn(context)
        .settings(
          name := "probing",
          //unmanagedClasspath in Compile += baseDirectory.value / "src" / "main" / "resources",

          libraryDependencies += "io.getquill" %% "quill-jdbc" % verQuill,
          libraryDependencies += "com.lihaoyi" %% "pprint" % "0.5.6"
        )
