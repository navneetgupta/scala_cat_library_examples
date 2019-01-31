package com.navneetgupta.scala.cat.foldable

import cats.Foldable
import cats.instances.list._
import cats.instances.option._

object CatsFoldableEx extends App {
  val ints = List(1, 2, 3, 4)

  println(Foldable[List].foldLeft(ints, 0)(_ + _))

  val mayBeInt = Option(23)
  println(Foldable[Option].foldLeft(mayBeInt, 10)(_ * _))
}

object CatsFoldableRightEx extends App {

  import cats.Eval
  import cats.instances.stream._

  def bigData = (1 to 100000).toStream

  // This below will cause Stack Overflow
  //println(bigData.foldRight(0L)(_ + _))

  //using Foldable.foldRight
  val eval: Eval[Long] =
    Foldable[Stream].
      foldRight(bigData, Eval.now(0L)) { (num, eval) =>
        eval.map(_ + num)
      }
  println(eval.value)

  /**
    * Foldable provides us with a host of useful methods defined on top of foldLeft.
    * Many of these are facimiles of familiar methods from the standard library:
    *
    * find, exists, forall, toList, isEmpty, nonEmpty, and so on:
    */
  println(Foldable[Option].nonEmpty(Option(42)))
  println(Foldable[List].find(List(1, 2, 3))(_ % 2 == 0))

  /**
    * Cats provides two methods that make use of Monoids:
    *       1.  combineAll (and its alias fold) combines all elements in the sequence using their Monoid;
    *       2.  foldMap maps a user-supplied func􏰁on over the sequence and combines the results using a Monoid.
    */

  import cats.instances.int._

  println(Foldable[List].combineAll(List(1, 2, 3)))

  import cats.instances.string._ // for Monoid
  println(Foldable[List].foldMap(List(1, 2, 3))(_.toString))

  import cats.instances.vector._ // for Monoid
  val ints = List(Vector(1, 2, 3), Vector(4, 5, 6))
  println((Foldable[List] compose Foldable[Vector]).combineAll(ints))
}

object CatsFoldableSyntax extends App {

  import cats.Eval
  import cats.instances.stream._
  import cats.syntax.foldable._
  import cats.instances.int._
  import cats.instances.string._

  println(List(1, 2, 3).combineAll)
  println(List(1, 2, 3).foldMap(_.toString))

  List(1, 2, 3).foldLeft(0)(_ + _)

}
