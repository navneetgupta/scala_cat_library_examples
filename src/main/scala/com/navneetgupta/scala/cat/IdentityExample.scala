package com.navneetgupta.scala.cat

import cats._

object IdentityExample extends App {

  import cats.implicits._
  import cats.Functor

  /**
    * The identity monad can be seen as the ambient monad that encodes the effect of having no effect. It is ambient in the sense that plain pure values are values of Id.
    *
    *
    */
  val x: Id[Int] = 1
  val y: Int = x

  val anId: Id[Int] = 42

  println(anId) // 42

  val one: Int = 1
  println(Functor[Id].map(one)(_ + 1)) // 2
  println(Applicative[Id].pure(42)) // 42

  //	def map[A, B](fa: Id[A])(f: A => B): Id[B]
  //	def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B]
  //	def coflatMap[A, B](a: Id[A])(f: Id[A] => B): Id[B]

  println(Monad[Id].map(one)(_ + 1)) // 2
  println(Monad[Id].flatMap(one)(_ + 1)) // 2

  val fortyTwo: Int = 42

  println(Comonad[Id].coflatMap(fortyTwo)(_ + 1)) // 3

}
