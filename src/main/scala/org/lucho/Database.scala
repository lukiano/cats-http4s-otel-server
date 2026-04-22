package org.lucho

import cats.effect.std.Console
import cats.effect.kernel.Temporal

import fs2.io.net.Network

import org.typelevel.otel4s.metrics.Meter
import org.typelevel.otel4s.trace.Tracer

import skunk._
import skunk.codec.all._
import skunk.implicits._

object Database {
    def session[F[_]: Temporal: Meter: Tracer: Network: Console] =
        Session.Builder[F]
            .withHost("localhost")    
            .withUserAndPassword("luciano", "Adri2300")
            .withDatabase("luciano")
            .single
            // .pooled(25)

    lazy val query = sql"select data from items".query(varchar)        
}
