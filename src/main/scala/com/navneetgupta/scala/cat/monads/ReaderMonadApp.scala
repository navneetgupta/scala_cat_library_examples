package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.Reader

object ReaderMonadApp extends App {

  case class Cat(name: String, favoriteFood: String)

  val catReader: Reader[Cat, String] = Reader.apply(cat => cat.name)
  println(catReader.run(Cat("Tom", "Milk")))

  /**
    * One common use for Readers is dependency injection.
    * The power of Readers comes from their map and flatMap methods, which represent different kinds of func􏰀tion composi􏰀tion.
    */

  val greetKitty: Reader[Cat, String] =
    catReader.map(name => s"Hello ${name}")

  println(greetKitty.run(Cat("Heathcliff", "junk food")))

  val feedKitty: Reader[Cat, String] =
    Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."

  println(greetAndFeed(Cat("Garfield", "lasagne")))
  println(greetAndFeed(Cat("Heathcliff", "junk food")))

}
