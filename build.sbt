name          := "balance-service-akka"
organization  := "qza"
description   := "simple balance service with akka"
version       := "1.0"

scalaVersion  := "2.11.7"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")


resolvers     += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val akkaVersion       = "2.3.12"
  val akkaHttpVersion   = "2.0-M2"

  Seq(
    "com.typesafe.akka"           %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"           %% "akka-http-experimental"            % akkaHttpVersion,
    "com.typesafe.scala-logging"  %% "scala-logging"                     % "3.1.0",
    "org.scalatest"               %% "scalatest"                         % "2.2.4"                 % "test",
    "ch.qos.logback"               % "logback-classic"                   % "1.1.3"
  )

}

exportJars    := true