package com.navneetgupta.scala.cat.functors

import cats._
import cats.implicits._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object CustomTypeFunctor {
  implicit val optionFunctor: Functor[Option] =
    new Functor[Option] {
      override def map[A, B](value: Option[A])(f: A => B): Option[B] = value.map(f)
    }

  implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
    new Functor[Future] {
      def map[A, B](value: Future[A])(f: A => B): Future[B] = value.map(f)
    }
}
