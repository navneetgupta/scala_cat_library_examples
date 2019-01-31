package com.navneetgupta.scala.cat.typeclasses

import cats._
import cats.implicits._

final case class Cat(name: String, age: Int, color: String)

object Cat {
  implicit val catShow: Show[Cat] =
    Show.show(cat => {
      val name = cat.name.show
      val age = cat.age.show
      val color = cat.color.show
      s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."
    })
}

object ShowCatApp extends App {
  println(Cat("Tom", 10, "Grey").show)
}
