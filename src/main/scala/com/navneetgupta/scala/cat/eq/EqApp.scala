package com.navneetgupta.scala.cat.eq

import cats.Eq
import cats.instances.int._
import cats.instances.option._

object EqApp extends App {
  val eqInt = Eq[Int]

  println(eqInt.eqv(123, 123))
  //println(eqInt.eqv(123, "123")) // return Compile time error due to type mismatch
  println(eqInt.eqv(123, 234))

  //println(123 === "123")

  import cats.syntax.eq._

  //Some(1) === None

  println((Some(1): Option[Int]) === (None: Option[Int]))

  println(Option(1) === Option.empty[Int])

  import cats.syntax.option._

  println(1.some === none[Int])
  println(1.some =!= none[Int])

}
