lazy val root = (project in file(".")).enablePlugins(JavaAgent)

name := "cats-http4s-otel-server"
description := "HTTP Scala server with Otel"

run / fork := true

val http4sVersion = "0.23.34"

libraryDependencies += "org.http4s" %% "http4s-ember-server" % http4sVersion
libraryDependencies += "org.http4s" %% "http4s-ember-client" % http4sVersion
libraryDependencies += "org.http4s" %% "http4s-dsl" % http4sVersion
libraryDependencies += "org.http4s" %% "http4s-circe" % http4sVersion

libraryDependencies += "org.typelevel" %% "log4cats-slf4j" % "2.8.0"
// libraryDependencies += "org.typelevel" %% "log4cats-natchez" % "0.2.0"

// val natchezVersion = "0.3.10"

// libraryDependencies += "org.tpolecat" %% "natchez-core" % natchezVersion
// libraryDependencies += "org.tpolecat" %% "natchez-log" % natchezVersion
// libraryDependencies += "org.tpolecat" %% "natchez-http4s" % "0.6.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.32"

libraryDependencies += "org.tpolecat" %% "skunk-core" % "1.0.0"

libraryDependencies += "org.typelevel" %% "mouse" % "1.4.0"

val otel4sVersion = "0.16.0"
libraryDependencies += "org.typelevel" %% "otel4s-oteljava" % otel4sVersion
libraryDependencies += "org.typelevel" %% "otel4s-instrumentation-metrics" % otel4sVersion
libraryDependencies += "org.typelevel" %% "otel4s-oteljava-context-storage" % otel4sVersion
// libraryDependencies += "org.typelevel" %% "otel4s-sdk-contrib-metrics" % "0.18.0"

libraryDependencies += "io.opentelemetry" % "opentelemetry-exporter-otlp" % "1.61.0" % Runtime
libraryDependencies += "io.opentelemetry" % "opentelemetry-sdk-extension-autoconfigure" % "1.61.0" % Runtime
libraryDependencies += "io.opentelemetry.instrumentation" % "opentelemetry-runtime-telemetry" % "2.26.1-alpha"

val circeVersion = "0.14.15"

// Optional for auto-derivation of JSON codecs
libraryDependencies += "io.circe" %% "circe-generic" % circeVersion
// Optional for string interpolation to JSON model
libraryDependencies += "io.circe" %% "circe-literal" % circeVersion

// Parser
libraryDependencies += "io.circe" %% "circe-parser" % circeVersion

// HTTP Client
libraryDependencies += "com.softwaremill.sttp.client4" %% "core" % "4.0.23"

val http4s_otel4s_middleware_version = "0.17.0"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-otel4s-middleware-core" % http4s_otel4s_middleware_version,
  "org.http4s" %% "http4s-otel4s-middleware-metrics" % http4s_otel4s_middleware_version,
  "org.http4s" %% "http4s-otel4s-middleware-trace-core" % http4s_otel4s_middleware_version,
  "org.http4s" %% "http4s-otel4s-middleware-trace-client" % http4s_otel4s_middleware_version,
  "org.http4s" %% "http4s-otel4s-middleware-trace-server" % http4s_otel4s_middleware_version,
)

scalaVersion := "3.8.3"

javaAgents += "io.github.irevive" % "otel4s-opentelemetry-javaagent" % "2.22.0" % Runtime

javaOptions += "-Dcats.effect.trackFiberContext=true"
javaOptions += "-Dotel.java.global-autoconfigure.enabled=true"