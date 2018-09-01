package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.{ Monad => Cmonad }

sealed trait Tree[+A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
case class Leaf[A](value: A) extends Tree[A]

object Tree {

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  implicit val treeMonad: Cmonad[Tree] = new Cmonad[Tree] {
    override def flatMap[A, B](opt: Tree[A])(fn: A => Tree[B]): Tree[B] = opt match {
      case Branch(left, right) => branch(flatMap(left)(fn), flatMap(right)(fn))
      case Leaf(value)         => fn(value)
    }
    override def pure[A](opt: A): Tree[A] =
      Leaf(opt)

    override def tailRecM[A, B](a: A)(fn: A => Tree[Either[A, B]]): Tree[B] =
      fn(a) match {
        case Leaf(Left(a1))  => tailRecM(a1)(fn)
        case Leaf(Right(b1)) => leaf(b1)
        case Branch(a, b) => {
          val left = flatMap(a) {
            case Left(l)  => tailRecM(l)(fn)
            case Right(r) => leaf(r)
          }
          val right = flatMap(b) {
            case Left(l)  => tailRecM(l)(fn)
            case Right(r) => leaf(r)
          }
          branch(left, right)
        }
      }
  }
}

object TreeMonad extends App {
  import Tree._

  println(leaf(100).flatMap(x => leaf(x * 2)))
  println(branch(leaf(100), leaf(200)).flatMap(x => branch(leaf(x - 1), leaf(x + 1))))
}
