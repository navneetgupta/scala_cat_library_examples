package com.navneetgupta.scala.cat.monads

import cats._
import cats.{ Monad => Cmonad }
import cats.implicits._

object IdentityMonad extends App {
  def sumSquare[F[_]: Cmonad](a: F[Int], b: F[Int]): F[Int] = a.flatMap(x => b.map(y => x * x + y * y))

  /**
   * Above method will work for Option,List
   * But not on plain values
   */

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1, 2, 3), List(4, 5)))

  // println(sumSquare(1, 2))  // will fail.

  /**
   * We need to be able to convert Plain value with something like identity function as Identity Monad
   *
   * Like below
   */

  println(sumSquare(3: Id[Int], 4: Id[Int]))

  /**
   * package cats
   * type Id[A] = A
   * Id is actually a type alias that turns an atomic type into a single-parameter type constructor.
   */

  //  implicit val idMonad: Cmonad[Id] =
  //    new Cmonad[Id] {
  //      override def pure[A](a: A): Id[A] = a
  //      override def flatMap[A, B](a: Id[A])(f: A => Id[B]): Id[B] = f(a)
  //      def map[A, B](a: Id[A])(f: A => B): Id[B] = f(a)
  //    }
}
