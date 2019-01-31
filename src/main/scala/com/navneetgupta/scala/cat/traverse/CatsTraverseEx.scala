package com.navneetgupta.scala.cat.traverse

import cats.Traverse
import cats.instances.future._
import cats.instances.list._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait Data {
  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com")

  // let's for demo
  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60)

  val numbers = List(Future(1), Future(2), Future(3))
}

object CatsTraverseSyntaxEx extends App with Data {

  import cats.syntax.traverse._

  // let's for demo
//  def getUptime(hostname: String): Future[Int] =
//    Future(hostname.length * 60)
//
//  println(Await.result(hostnames.traverse(getUptime), 1.second))
  val l = numbers.sequence
  println(Await.result(l, 2.second))

}

object CatsTraverseEx extends App with Data {

  val totalUptime: Future[List[Int]] = Traverse[List].traverse(hostnames)(getUptime)

  println(Await.result(totalUptime, 1.second))

  val numbers2: Future[List[Int]] =
    Traverse[List].sequence(numbers)

  println(Await.result(numbers2, 1.second))
}
