package com.navneetgupta.scala.cat

import cats._

object MonoidExample extends App {
  /*
   * Monoid extends the Semigroup type class, adding an empty method to semigroup's combine.
   * The empty method must return a value that when combined with any other instance of that
   * type returns the other instance,
   * */

  import cats.implicits._
  println(Monoid[String].empty)
  println(Monoid[String].combineAll(List("a", "b", "c")))
  println(Monoid[String].combineAll(List()))

  println(Monoid[Map[String, Int]].combineAll(List(Map("a" → 1, "b" → 2), Map("a" → 3))))
  println(Monoid[Map[String, Int]].combineAll(List()))

  val l = List(1, 2, 3, 4, 5)
  println(Monoid[Int].combineAll(List(1, 2, 3, 4, 5)))
  println(Monoid[Option[Int]].combineAll(List(Some(1), Some(2), Some(3), Some(4), Some(5))))
  println(Monoid[Option[Int]].combineAll(List(Some(1), Some(2), Some(3), Some(4), None)))
  println(l.foldMap(identity))
  println(l.foldMap(i ⇒ i.toString))

  println(l.foldMap(i ⇒ (i, i.toString)))
}
