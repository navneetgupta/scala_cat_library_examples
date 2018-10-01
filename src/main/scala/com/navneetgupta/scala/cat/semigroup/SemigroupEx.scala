package com.navneetgupta.scala.cat.semigroup

import cats.Semigroup
import cats.implicits._

object SemigroupEx extends App {
  import cats.syntax.either._ // for catchOnly

  // Either Monad
  def parseInt(str: String): Either[String, Int] = Either.catchOnly[NumberFormatException](str.toInt).
    leftMap(_ => s"Couldn't read $str")

  for {
    a <- parseInt("a")
    b <- parseInt("b")
    c <- parseInt("c")
  } yield (a + b + c)

  // the code above fails on the first call to parseInt and doesn’t go any further:

  //  trait Semigroupal[F[_]] {
  //    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  //  }

  // While Semigroup allows us to join values, Semigroupal allows us to join contexts.

  import cats.Semigroupal
  import cats.instances.option._ // for Semigroupal
  import cats.instances.list._

  println(Semigroupal[Option].product(Some(123), Some("abc"))) // Semigroupal acts on Context ie type classes not on value
  // res0: =Some(123, "abc")
  println(Semigroupal[Option].product(Option(1), Option(2)))
  //res0: =Some(1,2)
  println(Semigroupal[List].product(List(1, 2, 3), List("One", "Two", "Three")))
  //res0: =List((1,One), (1,Two), (1,Three), (2,One), (2,Two), (2,Three), (3,One), (3,Two), (3,Three))
  println(Semigroupal[Option].product(None, Some("abc")))
  //res1: =Option[(Nothing, String)] = None

  println(Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))) // While Semigroup works with Values.
  // res0: =List(1,2,3,4,5,6)
  println(Semigroup[Option[Int]].combine(Option(1), Option(2)))
  // res0: =Some(3)

  println(Semigroupal.tuple3(Option(1), Option(2), Option(3)))
  println(Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int]))
  println(Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _))
  println(Semigroupal.map2(Option(1), Option.empty[Int])(_ + _))

  import cats.syntax.apply._
  println((Option(123), Option("abc")).tupled)
  println((Option(123), Option("abc"), Option(true)).tupled)

  case class Cat(name: String, born: Int, color: String)
  println((Option("Garfield"), Option(1978), Option("Orange & black")).mapN(Cat.apply))

}
