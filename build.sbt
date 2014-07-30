organization := "ivmaykov.com.github"

name := "scala-json-benchmarks"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "3.2.6",
  "org.json4s" %% "json4s-ext" % "3.2.6",
  "org.json4s" %% "json4s-native" % "3.2.7",
  "io.spray" %%  "spray-json" % "1.2.6",
  "com.jayway.jsonpath" % "json-path" % "0.9.1")

assemblySettings