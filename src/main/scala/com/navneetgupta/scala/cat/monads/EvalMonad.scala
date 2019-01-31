package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._

object EvalMonad extends App {

  /**
    * Eager, Lazy, Memoized, Oh My!
    *
    * Scala vals are eager and memoized.
    */

  val x = {
    println("Computing X")
    math.random
  }
  // Defining x prints "Computing X" next time result is memoized, Check position of Computing X and the ================ in val result and lazy val result
  println("=======================")
  println(x)
  println(x)
  println(x)

  /**
    * By contrast, defs are lazy and not memoized.
    * The code to compute y below is not run un􏰀l we access it (lazy), and is re-run on every access (not memoized):
    */
  def y = {
    println("Computing X")
    math.random
  }

  println(y) //  this prints "Computing X"
  println(y) //  this prints "Computing X"
  println(y) //  this prints "Computing X"

  /**
    * lazy vals are lazy and memoized.
    */

  lazy val z = {
    println("Computing X")
    math.random
  }
  println("=======================")
  println(z) // Only invocation of this prints "Computing X" next time result is memoized
  println(z)
  println(z)

  val now = Eval.now {
    println("Computing Now")
    math.random + 1000
  } // now is like val—eager and memoized:
  val later = Eval.later {
    println("Computing Later")
    math.random + 2000
  } // later is like lazy-val-eager
  val always = Eval.always {
    println("Computing Always")
    math.random + 3000
  } // always is like def]

  println("=======================")
  println(now)
  println(now)
  println("=======================")
  println(later.value)
  println(later.value)
  println("=======================")
  println(always.value)
  println(always.value)
  println("=======================")
  /**
    * Like all monads, Eval's map and flatMap methods add computa􏰀ons to a chain.
    */
  val greeting = Eval.
    always {
      println("Step 1"); "Hello"
    }.
    map { str => println("Step 2"); s"$str world" }
  println("=======================")
  println(greeting.value)
  println("=======================")
  /**
    * while the seman􏰀cs of the origina􏰀ng Eval instances are maintained, mapping func􏰀ons are always called lazily on demand (def seman􏰀cs):
    */
  val ans = for {
    a <- Eval.now {
      println("Calculating A"); 40
    }
    b <- Eval.always {
      println("Calculating B"); 2
    }
  } yield {
    println("Adding A and B")
    a + b
  }
  println("=======================")
  println(ans.value)
  println(ans.value)
  println("=======================")

  // Important
  val saying = Eval.
    always {
      println("Step 1"); "The cat"
    }.
    map { str => println("Step 2"); s"$str sat on" }.
    memoize.
    map { str => println("Step 3"); s"$str the mat" }
  println("=======================")
  println(saying.value)
  println(saying.value)
  println("=======================")

  def factorial(n: BigInt): BigInt =
    if (n == 1) n else n * factorial(n - 1)

  //factorial(50000) this will stack overflow

  def factorial2(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(n)
    } else {
      Eval.defer(factorial2(n - 1).map(_ * n))
    }

  println(factorial2(50000).value)

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = as match {
    case head :: tail =>
      fn(head, foldRight(tail, acc)(fn))
    case Nil =>
      acc
  }

  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
      case Nil =>
        acc
    }

  def foldRight2[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = foldRightEval(as, Eval.now(acc))((a, b) => b.map(fn(a, _))).value

  //println(foldRight((1 to 100000).toList, 0L)(_ + _))  Stakc Overflow
  println(foldRight2((1 to 100000).toList, 0L)(_ + _))
}
