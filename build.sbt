ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

// more sbt examples in https://github.com/hhimanshu/sbt-getting-started

excludeLintKeys in Global ++= Set(idePackagePrefix)

lazy val root = (project in file("."))
  .settings(
    name := "FormulaMetrics",
    idePackagePrefix := Some("com.briefbytes.formulametrics"),
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.18",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
    ),
  )
