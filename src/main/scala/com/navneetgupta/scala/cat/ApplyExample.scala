package com.navneetgupta.scala.cat

import cats._

object ApplyExample extends App {

  import cats.implicits._

  //Already defined in the cats library
  implicit val optionApply: Apply[Option] = new Apply[Option] {
    def ap[A, B](f: Option[A => B])(fa: Option[A]): Option[B] =
      fa.flatMap(a => f.map(ff => ff(a)))

    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa map f

    override def product[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] =
      fa.flatMap(a => fb.map(b => (a, b)))
  }
  //Already defined in the cats library
  implicit val listApply: Apply[List] = new Apply[List] {
    def ap[A, B](f: List[A => B])(fa: List[A]): List[B] =
      fa.flatMap(a => f.map(ff => ff(a)))

    def map[A, B](fa: List[A])(f: A => B): List[B] = fa map f

    override def product[A, B](fa: List[A], fb: List[B]): List[(A, B)] =
      fa.zip(fb)
  }

  val intToString: Int ⇒ String = _.toString
  val double: Int ⇒ Int = _ * 2
  val addTwo: Int ⇒ Int = _ + 2

  println(Apply[Option].map(Some(1))(intToString)) // Some("1")
  println(Apply[Option].map(Some(1))(double)) //Some(2)
  println(Apply[Option].map(None)(addTwo)) // None

  //Compose
  val listOpt = Apply[List] compose Apply[Option]
  val plusOne = (x: Int) ⇒ x + 1
  println(listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3)))) // List(Some(2), None, Some(4))

  println(Apply[Option].ap(Some(intToString))(Some(1))) // Some("1")

  println(Apply[Option].ap(Some(double))(Some(1))) // Some(2)

  println(Apply[Option].ap(Some(double))(None)) // None
  println(Apply[Option].ap(None)(Some(1))) // None
  println(Apply[Option].ap(None)(None)) // None

  //AP2, AP3, ETC

  val addArity2 = (a: Int, b: Int) ⇒ a + b
  println(Apply[Option].ap2(Some(addArity2))(Some(1), Some(2))) // Some(3)
  println(Apply[Option].ap2(Some(addArity2))(Some(1), None)) // None

  val addArity3 = (a: Int, b: Int, c: Int) ⇒ a + b + c
  println(Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3))) // Some(6)

  //MAP2, MAP3, ETC

  println(Apply[Option].map2(Some(1), Some(2))(addArity2)) // Some(3)
  println(Apply[Option].map3(Some(1), Some(2), Some(3))(addArity3)) // Some(6)

  println(Apply[Option].tuple2(Some(1), Some(2))) // Some((1,2))
  println(Apply[Option].tuple3(Some(1), Some(2), Some(3))) // Some((1,2,3))

  val option2 = (Option(1), Option(2))
  val option3: (Option[Int], Option[Int], Option[Int]) = (Option(1), Option(2), Option.empty[Int])

  println(option2 mapN addArity2) // Some(3)
  println(option3 mapN addArity3) // None

  println(option2 apWith Some(addArity2)) // Some(3)
  println(option3 apWith Some(addArity3)) // None
  println(option2.tupled) // Some((1,2))
  println(option3.tupled) // None
}
