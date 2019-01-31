package com.navneetgupta.scala.cat.monads

import cats.{Monad => Cmonad}
import scala.annotation.tailrec

object CustomMonad extends App {
  /**
    * To define a Custom Monad
    * Need to define three fucntions:
    * flatMap
    * pure
    * tailRecM
    *
    * The tailRecM method is an optimization used in Cats to limit the amount of stack space consumed by
    * nested calls to flatMap. The technique comes from a 2015 paper by PureScript creator Phil Freeman.
    * The method should recursively call itself un􏰀l the result of fn returns a Right.
    */
  val optionMonad = new Monad[Option] {
    override def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] =
      opt flatMap fn

    override def pure[A](opt: A): Option[A] =
      Some(opt)

    @tailrec
    def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
      fn(a) match {
        case None => None
        case Some(Left(a1)) => tailRecM(a1)(fn)
        case Some(Right(b)) => Some(b)
      }
  }
}
