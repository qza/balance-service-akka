name := "balance-service-akka"
organization := "qza"
description := "simple balance service with akka"
version := "1.0"

scalaVersion := "2.11.7"
scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val akka_http_version = "2.0-M2"

  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akka_http_version,
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % akka_http_version,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akka_http_version,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" exclude("org.scala-lang", "scala-reflect"),
    "org.scalatest" %% "scalatest" % "2.2.4" % "test" exclude("org.scala-lang", "scala-reflect"),
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )

}

exportJars := true