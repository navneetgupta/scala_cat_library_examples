package com.navneetgupta.scala.cat.functors

import cats._
import cats.implicits._

sealed trait Tree[+A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
case class Leaf[A](value: A) extends Tree[A]

object Tree {

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  implicit val treeFunctor: Functor[Tree] =
    new Functor[Tree] {
      override def map[A, B](value: Tree[A])(f: A => B): Tree[B] = value match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(v)             => Leaf(f(v))
      }
    }
}

object TreeApp extends App {
  println(Tree.leaf(100).map(_ * 2))
  println(Tree.branch(Tree.leaf(1000), Tree.branch(Tree.leaf(101), Tree.leaf(201))).map(_ * 2))
}
