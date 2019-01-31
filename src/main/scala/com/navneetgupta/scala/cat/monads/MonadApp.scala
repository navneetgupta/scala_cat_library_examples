package com.navneetgupta.scala.cat.monads

trait Monad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  //map In terms of flatMap and pure ie. every Functor can be represented as a Monad, but not vice-versa
  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(x => pure(func(x)))
}

object MonadApp {

}
