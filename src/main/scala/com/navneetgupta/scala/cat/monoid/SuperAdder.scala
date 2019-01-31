package com.navneetgupta.scala.cat.monoid

import cats.{Monoid => Cmonoid}
import cats.instances.int._ // for Monoid
import cats.syntax.semigroup._
import cats.instances.option._

object SuperAdder extends App {

  def add[A](items: List[A])(implicit monoid: Cmonoid[A]): A = items.foldLeft(monoid.empty)(_ |+| _)

  def add2[A: Cmonoid](xs: List[A]): A =
    xs.foldLeft(Cmonoid[A].empty)(_ |+| _)

  println(add(List(1, 2, 3, 4, 5, 6)))
  println(add(List(): List[Int]))
  println(add(List(Option(1), Option(2), Option(3))))
  println(add(List(Some(1), None, Some(2), None, Some(3))))
  println(add2(List(1, 2, 3, 4, 5, 6)))
  println(add2(List(): List[Int]))
  println(add2(List(Option(1), Option(2), Option(3))))
  println(add2(List(Some(1), None, Some(2), None, Some(3))))

  case class Order(totalCost: Double, quantity: Double)

  implicit val orderMonoid: Cmonoid[Order] =
    new Cmonoid[Order] {
      def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)

      def empty = Order(0.0, 0.0)
    }

  println(add2(List(Order(12.0, 2.3), Order(14.23, 2.34))))

}
