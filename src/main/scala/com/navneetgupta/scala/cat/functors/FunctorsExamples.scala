package com.navneetgupta.scala.cat.functors

//import scala.language.higherKinds
//import cats.Functor
//import cats.instances.list._ // for Functor
//import cats.instances.option._
//import cats.instances.function._
//import cats.syntax.functor._
import cats._
import cats.implicits._

object FunctorsExamples extends App {
  val list1 = List(1, 2, 3, 4, 5)

  println(Functor[List].map(list1)(_ * 4))
  val option1 = Option(123)

  println(Functor[Option].map(option1)(_ * 5))

  val func1 = (x: Int) => x + 1
  val liftedFunct = Functor[Option].lift(func1)

  liftedFunct.apply(Option(123)).show

  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => a + "!"
  val func4 = func1.map(func2).map(func3)
  func4(123).show

  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  doMath(Option(20)).show
  doMath(List(1, 2, 3)).show
}
