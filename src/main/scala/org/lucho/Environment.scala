package org.lucho

import cats.{ Functor, MonadThrow }

import cats.effect.std.Env

import cats.syntax.all._

import io.circe.parser.decode

type Config = Map[String, String]

import cats.mtl.Ask
type ConfigProvider[F[_]] = Ask[F, Config]

val environmentKey = "lucho"

def retrieveEnvironmentConfig[F[_]: Env: Functor]: F[Config] =
    for {
        maybeJsonString <- Env[F].get(environmentKey)
        result = decode[Map[String, String]](maybeJsonString.getOrElse("{}"))
        environmentMap = result.getOrElse(Map())
    } yield environmentMap

// def toConfigProvider[F[_]: Env: Functor]: F[ConfigProvider[F]] = ???


def retrieveConfigValue[F[_]: ConfigProvider: MonadThrow](key: String): F[String] =
    for {
        config <- Ask.apply.ask
        maybeValue = config.get(key)
        value <- maybeValue.liftTo(new NoSuchElementException(s"Key $key not found in config"))
    } yield value
