package com.navneetgupta.scala.cat

import cats.implicits._

object OptionalEx extends App {
  val a = 3.some

  println(3.some.combineK(4.some).combineK(5.some).combineK(none[Int]))
  println(none[Int].combineK(4.some).combineK(5.some).combineK(none))
  println(none[Int].combineK(none[Int]).combineK(5.some).combineK(none))
  println(none[Int].combineK(none[Int]).combineK(none[Int]).combineK(6.some))
  println(none[Int].combineK(none[Int]).combineK(none[Int]).combineK(none))
}
