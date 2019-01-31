package com.navneetgupta.scala.cat

import cats.Semigroup
//import scalaz._

object SemiGroupEx2 extends App {

  /*
   * A semigroup for some given type A has a single operation (which we will call combine), which takes two values of type A,
   * and returns a value of type A. This operation must be guaranteed to be associative.
   * */

  import cats.implicits._

  println(Semigroup[Int].combine(1, 2))
  println(Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)))
  println(Semigroup[Option[Int]].combine(Option(1), Option(2)))
  println(Semigroup[Option[Int]].combine(Option(1), None))
  println(Semigroup[List[Int]].combine(Nil, List(1, 2)))
  println(Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6))

  //Check Usefulness of below two operations
  // First one uses Semigroup.combine funcion
  // Second one uses scala native ++ on map
  println("================Check the Diffrence between below two============================")
  println(Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map())))
  println(Map("foo" -> Map("bar" -> 5)) ++ Map("foo" -> Map("bar" -> 6), "baz" -> Map()))
  println("=================================================================================")
  //Check Usefulness of below two operations
  // First one uses Semigroup.combine funcion
  // Second one uses scala native ++ on map
  println("================Check the Diffrence between below two============================")
  println(Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42))))
  println(Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3, 4), "bar" -> List(42)))

  println("=================================================================================")

  val aMap = Map("foo" → Map("bar" → 5))
  val anotherMap = Map("foo" → Map("bar" → 6))
  val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)

  println(combinedMap)

  val one: Option[Int] = Option(1)
  val two: Option[Int] = Option(2)
  val n: Option[Int] = None

  println(one |+| two)
  println(one |+| n)
  println(two |+| n)
  println(n |+| n)
  println(n |+| two)
}
