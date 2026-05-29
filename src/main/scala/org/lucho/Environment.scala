package org.lucho

import cats.MonadThrow

import cats.effect.std.Env

import cats.syntax.all._

import io.circe.parser.decode

type Config = Map[String, String]

private def retrieveEnvironmentConfig[F[_]: Env as e: MonadThrow]: F[Config] =
    for {
        maybeJsonString <- e.get("lucho")
        jsonString <- maybeJsonString.liftTo(new RuntimeException("Environment variable 'lucho' not found"))
        result = decode[Map[String, String]](jsonString)
        environmentMap <- result.liftTo[F]
    } yield environmentMap


def retrieveConfigValue[F[_]: Env: MonadThrow](key: String): F[String] =
    for {
        config <- retrieveEnvironmentConfig
        maybeValue = config.get(key)
        value <- maybeValue.liftTo(new NoSuchElementException(s"Key $key not found in config"))
    } yield value
