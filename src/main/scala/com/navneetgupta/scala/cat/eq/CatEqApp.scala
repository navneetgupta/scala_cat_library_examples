package com.navneetgupta.scala.cat.eq

import cats._
import cats.implicits._

final case class Cat(name: String, age: Int, color: String)

object Cat {
  implicit val catEq: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      (cat1.name === cat2.name) && (cat1.age === cat2.age) && (cat1.color === cat2.color)
    }
}

object CatEqApp extends App {

  val cat1 = Cat("Garfield", 38, "Orange and Black")
  val cat3 = Cat("Garfield", 38, "Orange and Black")
  val cat2 = Cat("Heathcliff", 33, "Orange and Black")

  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]

  println(cat1 === cat2)
  println(cat1 === cat3)
  println(cat1 =!= cat2)
  println(optionCat1 === optionCat2)

}
