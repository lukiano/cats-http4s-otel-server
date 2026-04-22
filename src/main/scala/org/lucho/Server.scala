package org.lucho

import com.comcast.ip4s.{ Host, Port }

import cats.effect.std.Console
import cats.effect.kernel.{ Async, Resource }

import fs2.io.net.Network

import org.http4s.ember.server.EmberServerBuilder

import org.http4s.server.{ Server => Http4sServer }

import org.typelevel.otel4s.metrics.Meter
import org.typelevel.otel4s.trace.Tracer

object Server {
  def server[F[_]: Async: Meter: Tracer: Network: Console](host: Host, port: Port): Resource[F, Http4sServer] =
    EmberServerBuilder.default[F]
      .withHost(host)
      .withPort(port)
      .withHttpApp(Routes.routes().orNotFound)
      .build
}
