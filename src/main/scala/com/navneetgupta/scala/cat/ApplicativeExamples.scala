package com.navneetgupta.scala.cat

import cats._

object ApplicativeExamples extends App {
  import cats.implicits._
  /**
   * Applicative extends Apply by adding a single method, pure:
   * def pure[A](x: A): F[A]	
   * This wraps a value in context of the functor
   * */
  
  println(Applicative[Option].pure(1)) // Some(1)
  println(Applicative[List].pure(1)) // List(1)
  println((Applicative[List] compose Applicative[Option]).pure(1)) // List(Some(1))
  println(Monad[Option].pure(1)) //Some(1)
  println(Applicative[Option].pure(1)) //Some(1)
  
}