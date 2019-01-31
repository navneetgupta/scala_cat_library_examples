package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.State

object PostOrderCalculator extends App {
  type CalcState[A] = State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] =
    sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }

  def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
      (num :: stack, num)
    }

  def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
      case a :: b :: tail =>
        val ans = func(a, b)
        (ans :: tail, ans)
      case _ =>
        sys.error("Fail!")
    }

  println(evalOne("42").runA(Nil).value)

  val program = for {
    _ <- evalOne("1")
    _ <- evalOne("2")
    ans <- evalOne("+")
  } yield ans

  println(program.runA(Nil).value)

  def evalAll(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState]) { (a, b) =>
      a.flatMap(_ => evalOne(b))
    }

  val program1 = evalAll(List("1", "2", "+", "3", "*"))

  println(program1.runA(Nil).value)

  val program2 = for {
    _ <- evalAll(List("1", "2", "+"))
    _ <- evalAll(List("3", "4", "+"))
    ans <- evalOne("*")
  } yield ans

  println(program2.runA(Nil).value)

  def evalInput(input: String): Int = evalAll(input.split(" ").toList).runA(Nil).value

  println(evalInput("1 2 + 3 4 + *"))
}
