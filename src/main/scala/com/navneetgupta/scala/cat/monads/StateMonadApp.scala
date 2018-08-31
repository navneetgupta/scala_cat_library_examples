package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.State

object StateMonadApp extends App {

  /**
   * State[S, A] is a function S => (S,A)
   * where A is response/result type
   *
   * i.e.
   * , an instance of State is a func􏰀on that does two things:
   *       • transforms an input state to an output state;
   *       • computes a result.
   */
  val a = State[Int, String] { state =>
    (state, s"The state is $state")
  }

  println(a)

  /**
   * State provides three methods—run, runS, and runA—that return different combina􏰀ons of state and result.
   * Each method returns an instance of Eval, which State uses to maintain stack safety.
   */

  val (state, result) = a.run(10).value
  println(state + "    " + result)
  val state1 = a.runS(10).value
  println(state1)
  val result1 = a.runA(10).value
  println(result1)

  /**
   * As we’ve seen with Reader and Writer, the power of the State monad comes from combining instances.
   * The map and flatMap methods thread the state from one instance to another.
   */

  val step1 = State[Int, String] { num =>
    val ans = num + 1
    (ans, s"Result of step1: $ans")
  }
  val step2 = State[Int, String] { num =>
    val ans = num * 2
    (ans, s"Result of step2: $ans")
  }

  val both = for {
    a <- step1
    b <- step2
  } yield (a, b)
  println(both)

  val (state2, result2) = both.run(20).value
  println(state2 + "    " + result2)

  val getDemo = State.get[Int]
  println(getDemo.run(10).value)

  val setDemo = State.set[Int](30)
  println(setDemo.run(10).value)

  val pureDemo = State.pure[Int, String]("Result")
  println(pureDemo.run(10).value)

  val inspectDemo = State.inspect[Int, String](_ + "!")
  println(inspectDemo.run(10).value)

  val modifyDemo = State.modify[Int](_ + 1)
  println(modifyDemo.run(10).value)

  val program: State[Int, (Int, Int, Int)] = for {
    a <- State.get[Int]
    _ <- State.set[Int](a + 1)
    b <- State.get[Int]
    _ <- State.modify[Int](_ + 1)
    c <- State.inspect[Int, Int](_ * 1000)
  } yield (a, b, c)

  val (state3, result3) = program.run(1).value

  println(state3 + "    " + result3)
}
