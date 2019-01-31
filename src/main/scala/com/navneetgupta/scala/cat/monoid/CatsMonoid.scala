package com.navneetgupta.scala.cat.monoid

import cats._
import cats.implicits._

object CatsMonoid extends App {

  // String Monoid
  println(Monoid[String].combine("Hi ", "there"))
  println(Monoid[String].empty)

  println(Monoid.apply[String].combine("Hi ", "there"))
  println(Monoid.apply[String].empty)

  //String SemiGroup
  println(Semigroup[String].combine("Hi ", "there"))

  //Int Monoid
  println(Monoid[Int].combine(10, 20))
  println(Monoid[Int].empty)

  //Option Monoid
  println(Monoid[Option[Int]].combine(Option(20), Option(43)))
  println(Monoid[Option[Int]].empty)

  /**
    * Cats provides syntax for the combine method in the form of the |+| operator.
    * Because combine technically comes from Semigroup,
    * we access the syntax by impor􏰀ng from cats.syntax.semigroup:
    */

  import cats.syntax.semigroup._

  println("Hi " |+| "there" |+| Monoid[String].empty)
  println(1 |+| 2 |+| Monoid[Int].empty)
  println(Option(32) |+| Option(33) |+| Option.empty[Int])

  val map1 = Map("a" -> 1, "b" -> 2)
  val map2 = Map("b" -> 3, "d" -> 4)
  println(map1 |+| map2)

  val tuple1 = ("hello", 123)
  val tuple2 = ("world", 321)
  println(tuple1 |+| tuple2)
}
