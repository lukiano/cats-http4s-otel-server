package org

import org.typelevel.log4cats._
// assumes dependency on log4cats-slf4j module
import org.typelevel.log4cats.slf4j.Slf4jFactory

package object lucho {
  
    // create our LoggerFactory
    // implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]

}
