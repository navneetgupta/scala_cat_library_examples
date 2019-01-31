package com.navneetgupta.scala.cat

import cats._

object FoldableExamples extends App {

  import cats.implicits._

  /**
    * For DS that can be folded over like  List/Set
    *
    */

  println(Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _)) //6
  println(Foldable[List].foldLeft(List("a", "b", "c"), "")(_ + _)) // "abc"
  val lazyResponse = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
  println(lazyResponse.value) // 6

  println(Foldable[List].fold(List("a", "b", "c"))) // "abc"

  println(Foldable[List].fold(List(1, 2, 3))) // 6
  println(Foldable[List].foldMap(List("a", "b", "c"))(_.length)) //3

  println(Foldable[List].foldMap(List(1, 2, 3))(_.toString)) // "123"

  println(Foldable[List].foldK(List(List(1, 2), List(3, 4, 5)))) // List(1,2,3,4,5)

  println(Foldable[List].foldK(List(None, Option("two"), Option("three")))) // Some("two")

  println(Foldable[List].find(List(1, 2, 3))(_ > 2)) // Some(3)
  println(Foldable[List].find(List(1, 2, 3))(_ > 5)) // None
  println(Foldable[List].exists(List(1, 2, 3))(_ > 5)) //false
  println(Foldable[List].exists(List(1, 2, 3))(_ > 2)) // true

  println(Foldable[List].forall(List(1, 2, 3))(_ <= 3)) // true
  println(Foldable[List].forall(List(1, 2, 3))(_ < 3)) // false

  println(Foldable[List].toList(List(1, 2, 3))) //List(1,2,3)
  println(Foldable[Option].toList(Option(42))) // List(42)
  println(Foldable[Option].toList(None)) // List()

  println(Foldable[List].filter_(List(1, 2, 3))(_ < 3)) // List(1,2)
  println(Foldable[Option].filter_(Option(42))(_ != 42)) // List()

  def parseInt(s: String): Option[Int] = Either.catchOnly[NumberFormatException](s.toInt).toOption

  println(Foldable[List].traverse_(List("1", "2", "3"))(parseInt)) // Some(())
  println(Foldable[List].traverse_(List("a", "b", "c"))(parseInt)) // None

  val FoldableListOption = Foldable[List].compose[Option]

  println(FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4)))) // 10
  println(FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3")))) // "123"

  println(Foldable[List].isEmpty(List(1, 2, 3))) // false

  println(Foldable[List].dropWhile_(List(1, 2, 3))(_ < 2)) // List(2,3)

  println(Foldable[List].takeWhile_(List(1, 2, 3))(_ < 2)) //List(1)
}
