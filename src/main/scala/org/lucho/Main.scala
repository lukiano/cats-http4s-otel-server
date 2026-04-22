package org.lucho

import cats.effect.{ LiftIO, IO, IOApp }

import cats.effect.kernel.{ Async, Resource }

import cats.effect.std.Console

import com.comcast.ip4s.{host, port}

import fs2.io.net.Network

import org.typelevel.otel4s.instrumentation.ce.IORuntimeMetrics
import org.typelevel.otel4s.metrics.{ Meter, MeterProvider }
import org.typelevel.otel4s.trace.{ Tracer, TracerProvider }

import org.typelevel.otel4s.oteljava.OtelJava

import org.typelevel.otel4s.context.LocalProvider
import org.typelevel.otel4s.oteljava.context.{ Context, IOLocalContextStorage }

object Main extends IOApp.Simple {

    def build[F[_]: Async: Network: Console: LiftIO] = {
      // given LocalProvider[F, Context] = IOLocalContextStorage.localProvider[F]
      for {
          otel4s <- OtelJava.autoConfigured[F]()
          given MeterProvider[F] = otel4s.meterProvider
          _ <- IORuntimeMetrics.register[F](runtime.metrics, IORuntimeMetrics.Config.default)
          given Meter[F] <- Resource.eval(otel4s.meterProvider.get("cats-http4s-otel-server"))
          given Tracer[F] <- Resource.eval(otel4s.tracerProvider.get("cats-http4s-otel-server"))
          server <- Server.server[F](host"localhost", port"8080")

      } yield server
    }

    override def run: IO[Unit] = build[IO].useForever
}

