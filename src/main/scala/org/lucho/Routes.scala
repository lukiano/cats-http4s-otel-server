package org.lucho

import cats.MonadThrow

import cats.effect.std.{ Console, Env }
import cats.effect.kernel.Temporal

import io.circe.syntax._

import fs2.io.net.Network

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import org.http4s.otel4s.middleware.client.UriTemplateClassifier
import org.http4s.otel4s.middleware.server.RouteClassifier

import org.http4s.circe._

import org.typelevel.otel4s.metrics.Meter
import org.typelevel.otel4s.trace.Tracer

import cats.Functor

object Routes {
  
    def routeClassifier[F[_]: Env: MonadThrow]: F[RouteClassifier] = {
        val http4sDsl = Http4sDsl[F]
        import http4sDsl._
        import cats.syntax.functor._

        for {
            routeName <- retrieveConfigValue("hello")
        } yield RouteClassifier.of {
            case GET -> Root / routeName =>
                "/hello"
        }        
    }

    def routes[F[_]: Console: Env: Meter: Network: Temporal: Tracer](): HttpRoutes[F] = {
        val http4sDsl = Http4sDsl[F]
        import http4sDsl._
        import cats.Monad.ops._

        HttpRoutes.of[F] {
            case GET -> Root / "hello" =>
                for {
                    result <- Database.session.use(conn => conn.execute(Database.query))
                    response <- Ok(result.asJson)
                } yield response
        }
    }
}
