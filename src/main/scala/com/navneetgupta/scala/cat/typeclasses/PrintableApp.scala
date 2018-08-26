package com.navneetgupta.scala.cat.typeclasses

object PrintableApp extends App {

  final case class Cat(name: String, age: Int, color: String)
  implicit val printableCat: Printable[Cat] =
    new Printable[Cat] {
      override def format(value: Cat) = s"${value.name} is a ${value.age} year-old ${value.color} cat."
    }

  val c = Cat("Tom", 10, "White")

  println(Printable.format(c))
  Printable.print(c)

  import PrintableSyntax._
  println(c.format)
  c.print
}
