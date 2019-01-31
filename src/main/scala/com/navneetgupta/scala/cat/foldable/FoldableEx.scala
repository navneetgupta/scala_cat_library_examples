package com.navneetgupta.scala.cat.foldable

import cats.Monoid

object FoldableEx extends App {

  def show[A](list: List[A]): String = list.foldLeft("nil")((accum, item) => s"$item then $accum")

  def showRight[A](list: List[A]): String = list.foldRight("nil")((item, accum) => s"$item then $accum")

  println(show(Nil))
  println(show(List(1, 2, 3)))
  println(show(List("Ram", "Shyam", "Ghanshyam")))
  println(showRight(Nil))
  println(showRight(List(1, 2, 3)))
  println(showRight(List("Ram", "Shyam", "Ghanshyam")))

  def cons1[A](list: List[A]): List[A] = list.foldLeft(Nil: List[A])((acc, item) => item :: acc)

  def cons2[A](list: List[A]): List[A] = list.foldRight(Nil: List[A])((item, acc) => item :: acc)

  println(cons1(Nil))
  println(cons1(List(1, 2, 3)))
  println(cons1(List("Ram", "Shyam", "Ghanshyam")))
  println(cons2(Nil))
  println(cons2(List(1, 2, 3)))
  println(cons2(List("Ram", "Shyam", "Ghanshyam")))

  def map[A, B](list: List[A], fun: A => B): List[B] = list.foldRight(Nil: List[B])((item, acc) => fun(item) :: acc)

  def flatMap[A, B](list: List[A], fun: A => List[B]): List[B] = list.foldRight(Nil: List[B])((item, acc) => fun(item) ++ acc)

  val l = List(1, 2, 3)

  def double(x: Int): Int = x * 2

  println(map(l, double))

  def toList(x: Int): List[Int] = List(x * 4)

  println(flatMap(l, toList))

  def filter[A](list: List[A], predicate: A => Boolean): List[A] = list.foldRight(Nil: List[A])((item, acc) => if (predicate(item)) item :: acc else acc)

  def sum[A](list: List[A])(implicit monoid: Monoid[A]): A = list.foldRight(monoid.empty)(monoid.combine)
}
