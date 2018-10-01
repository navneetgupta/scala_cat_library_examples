package com.navneetgupta.scala.cat.functors

object ContraVariantFunctorEx extends App {

  /**
   * Contravariant Functor type class represents prepending an operation to a chain
   * These Functor provides an operation 'contramap' that represents prepending an operation to a chain.
   *
   * contramap operation makes most sense for data type that represents transformation.
   *
   * For Ex: Option does not make sense with contramap operation since we cannot feed a value  in
   * Option[B] backwards through a fucntion A => B
   *
   */

  trait Printable[A] {
    self =>

    def format(value: A): String
    def contramap[B](funct: B => A): Printable[B] = new Printable[B] {
      override def format(value: B): String = self.format(funct(value))
    }
  }

  /**
   * Above trait represents a transformation from A => String.
   *
   * Its Contramap operation accepts a function of type B => A and creates a new Printable[B]
   */

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(value: String): String = "\"" + value + "\""
  }

  implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    override def format(value: Boolean): String = if (value) "\"" + "yes" + "\"" else "\"" + "no" + "\""
  }

  println(format("hello"))
  println(format(true))
  println(format(false))

  final case class Box[A](value: A)

  implicit def boxPrintable[A](implicit p: Printable[A]) = p.contramap[Box[A]](a => a.value)

  println(format(Box("hello")))
  println(format(Box(true)))
  println(format(Box(false)))

}
