package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.Writer

object WritterMonadApp extends App {


  val w = Writer(Vector(
    "It was the best of the time",
    "It wan not the best time of the Year"), 1859)

  println(w)

  //For convenience, Cats provides a way of crea􏰀ng Writers specifying only the log or the result.
  //If we only have a result we can use the standard pure syntax.
  type Logged[A] = Writer[Vector[String], A]
  println(123.pure[Logged])

  //If we have a log and no result we can create a Writer[Unit] using the tell syntax
  println(Vector("msg1", "msg2", "msg3").tell)

  //If we have both a result and a log, we can either use Writer.apply or we can use the writer syntax
  val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
  val b = 123.writer(Vector("msg1", "msg2", "msg3"))

  println(a)
  println(b)

  /**
    * We can extract the result and log from a Writer using the value and written methods respec􏰀vely:
    * We can extract both values at the same 􏰀me using the run method:
    */

  // Extract Result
  val aResult: Int =
  a.value
  // Extract Errors
  val aLog: Vector[String] =
    a.written

  // Extract Both
  val (log, result) = a.run

  println(aResult)
  println(aLog)
  println(log)
  println(result)

  // Composing and transforming Writers.
  val writer1 = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b

  println(writer1.run)

  /**
    * In addi􏰀on to transforming the result with map and flatMap, we can transform the log in a Writer with the mapWritten method:
    *
    */

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))

  println(writer2.run)

  /**
    * We can transform both log and result simultaneously using bimap or mapBoth.
    * 		1.	bimap takes two func􏰀on parameters, one for the log and one for the result.
    * 		2.	mapBoth takes a single func􏰀on that accepts two parameters:
    */

  val writer3 = writer1.bimap(
    log => log.map(_.toUpperCase),
    res => res * 100
  )

  val writer4 = writer1.mapBoth { (log, res) =>
    val log2 = log.map(_ + "!")
    val res2 = res * 1000
    (log2, res2)
  }

  println(writer3.run)
  println(writer4.run)


  /**
    * Finally, we can clear the log with the reset method and swap log and result with the swap method:
    */

  val writer5 = writer1.reset

  val writer6 = writer1.swap

  println(writer5.run)
  println(writer6.run)
}
