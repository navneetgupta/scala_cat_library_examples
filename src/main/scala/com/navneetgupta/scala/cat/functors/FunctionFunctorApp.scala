package com.navneetgupta.scala.cat.functors

import cats.instances.function._
import cats.syntax.functor._

object FunctionFunctorApp extends App {
  val func1: Int => Double =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (x: Double) => x * 2

  println((func1 map func2)(1))
  println((func1 andThen func2)(1))
  println((func2(func1(1))))

  val func =
    ((x: Int) => x.toDouble).
      map(x => x + 1).
      map(x => x * 2).
      map(x => x + "!")
  println(func(123))
}
