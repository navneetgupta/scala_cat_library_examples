package com.navneetgupta.scala.cat.monads

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats._
import cats.implicits._
import cats.data.Writer

object FactorialWithWriterMonad extends App {

  def slowly[A](body: => A) =
    try
      body
    finally
      Thread.sleep(100)

  def factorial(n: Int): Int = {
    val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
    println(s"fact $n $ans")
    ans
  }

  Await.result(Future.sequence(Vector(
    Future(factorial(3)),
    Future(factorial(3)))), 5.seconds)

  type Logged[A] = Writer[Vector[String], A]

  def factorial2(n: Int): Logged[Int] =
    for {
      ans <- if (n == 0) {
        1.pure[Logged]
      } else {
        slowly(factorial2(n - 1).map(_ * n))
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans

  val (log, res) = factorial2(5).run

  println(log)
  println(res)

  //We can run several factorials in parallel as follows, capturing their logs in- dependently without fear of interleaving:
  val Vector((logA, ansA), (logB, ansB)) =
    Await.result(Future.sequence(Vector(
      Future(factorial2(3).run),
      Future(factorial2(5).run))), 5.seconds)

  println(logA)
  println(ansA)
  println(logB)
  println(ansB)

}
