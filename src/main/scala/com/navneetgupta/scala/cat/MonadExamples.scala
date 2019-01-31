package com.navneetgupta.scala.cat

import cats._

object MonadExamples extends App {

  import cats.implicits._

  /**
    * Monad Extends the applicative type classes with a new method "flatten" which joins nested context in a single conext
    *
    * F[F[A]] => F[A]
    *
    */

  println(Option(Option(1)).flatten) //Some(1) / Option(1)
  println(Option(None).flatten) // None
  println(List(List(1), List(2, 3)).flatten) // List(1,2,3)
  println(Monad[Option].pure(42)) // Some(42)
  println(Monad[List].flatMap(List(1, 2, 3))(x => List(x, x))) // List(1,1,2,2,3,3)
  println(Monad[Option].ifM(Option(false))(Option("truthy"), Option("falsy"))) // Some("falsy")
  println(Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))) // Some("truthy")
  println(Monad[List].ifM(List(false, false, true))(List(1, 2), List(3, 4))) // List(3,4,3,4,1,2)   use List(1,2) if true else use List(3,4) => false,false,true => (List(3,4), List(3,4),List(1,2)).flatten
  println(Monad[List].ifM(List(false, false, false))(List(1, 2), List(3, 4))) // List(3,4,3,4,3,4)   use List(1,2) if true else use List(3,4) => false, false, false => (List(3,4), List(3,4),List(3,4)).flatten
  println(Monad[List].ifM(List(true, true, true))(List(1, 2), List(3, 4))) // List(1,2,1,2,1,2)   use List(1,2) if true else use List(3,4) => true,true,true => (List(1,2), List(1,2),List(1,2)).flatten
  println(Monad[List].ifM(List(true, true, false))(List(1, 2), List(3, 4))) // List(1,2,1,2,3,4)   use List(1,2) if true else use List(3,4) => true, true, false => (List(1,2), List(1,2),List(3,4)).flatten
  println(Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4))) // List(1,2,3,4,1,2)   use List(1,2) if true else use List(3,4) => true, false, true => (List(1,2), List(3,4),List(1,2)).flatten
  println(Monad[List].ifM(List(true, false, false))(List(1, 2), List(3, 4))) // List(1,2,3,4,3,4)   use List(1,2) if true else use List(3,4) => true, false, false => (List(1,2), List(3,4), List(3,4)).flatten
  println(Monad[List].ifM(List(false, true, true))(List(1, 2), List(3, 4))) // List(3,4,1,2,1,2)   use List(1,2) if true else use List(3,4) => false, true,  true => (List(3,4), List(1,2),List(1,2)).flatten
  println(Monad[List].ifM(List(false, true, false))(List(1, 2), List(3, 4))) // List(3,4,1,2,3,4)   use List(1,2) if true else use List(3,4) => false, true,  false => (List(3,4),List(1,2), List(3,4)).flatten

}
