package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.Writer

object WritterMonadApp extends App {
  val w = Writer(Vector(
    "It was the best of the time",
    "It wan not the best time of the time"), 1859)

  println(w)
}
