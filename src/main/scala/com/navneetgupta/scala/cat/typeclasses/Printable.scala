package com.navneetgupta.scala.cat.typeclasses

trait Printable[A] {
  def format(value: A): String
}

object PrintalbleInstances {
  implicit val printableString: Printable[String] =
    new Printable[String] {
      override def format(value: String) = value
    }
  implicit val printlableInt: Printable[Int] =
    new Printable[Int] {
      override def format(value: Int) = value.toString()
    }
}

object Printable {
  def format[A](value: A)(implicit printable: Printable[A]): String = printable.format(value)

  def print[A](value: A)(implicit printable: Printable[A]): Unit = println(printable.format(value))
}

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String =
      p.format(value)

    def print(implicit p: Printable[A]): Unit = println(format(p))
  }

}
