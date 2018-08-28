package com.navneetgupta.scala.cat.monads

import cats._
import cats.{ Monad => Cmonad }
import cats.implicits._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
//import cats.syntax.functor._
//import cats.syntax.flatMap._

object CatsMonad extends App {
  println(Cmonad[Option].pure(3))

  val opt2 = Cmonad[Option].flatMap(Cmonad[Option].pure(3))(a => Some(a + 2))
  println(opt2)
  val opt3 = Cmonad[Option].map(opt2)(a => 100 * a)
  println(opt3)

  val list1 = Cmonad[List].pure(3)

  println(list1)

  val list2 = Cmonad[List].
    flatMap(List(1, 2, 3))(a => List(a, a * 10))

  println(list2)

  val list3 = Cmonad[List].map(list2)(a => a + 123)
  println(list3)

  /**
   *
   * Unlike the methods on the Future class itself, the pure and flatMap methods on the monad can’t accept implicit
   * ExecutionContext parameters (because the parameters aren’t part of the defini􏰀ons in the Monad trait).
   * To work around this, Cats requires us to have an ExecutionContext in scope when we summon a Monad for Future:
   *
   */
  import scala.concurrent.ExecutionContext.Implicits.global
  val fm = Cmonad[Future]

  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  Await.result(future, 1.second)

  def sumSquare[F[_]: Cmonad](a: F[Int], b: F[Int]): F[Int] = a.flatMap(x => b.map(y => x * x + y * y))

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1, 2, 3), List(4, 5)))

  def sumSquare2[F[_]: Cmonad](a: F[Int], b: F[Int]): F[Int] = for {
    x <- a
    y <- b
  } yield x * x + y * y

  println(sumSquare2(Option(3), Option(4)))
  println(sumSquare2(List(1, 2, 3), List(4, 5)))
}
