package org.lucho

import cats.{ Functor, MonadThrow }

import cats.effect.std.Env

import cats.syntax.all._

import io.circe.parser.decode

type Config = Map[String, String]

val environmentKey = "lucho"

private def retrieveEnvironmentConfig[F[_]: Env: Functor]: F[Config] =
    for {
        maybeJsonString <- Env[F].get(environmentKey)
        result = decode[Map[String, String]](maybeJsonString.getOrElse("{}"))
        environmentMap = result.getOrElse(Map())
    } yield environmentMap


def retrieveConfigValue[F[_]: Env: MonadThrow](key: String): F[String] =
    for {
        config <- retrieveEnvironmentConfig
        maybeValue = config.get(key)
        value <- maybeValue.liftTo(new NoSuchElementException(s"Key $key not found in config"))
    } yield value
