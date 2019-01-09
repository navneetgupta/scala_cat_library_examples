package com.navneetgupta.scala.cat.traverse

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object TraverseEx extends App {
  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com")

  // let's for demo
  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60)

  /**
   * to poll all of the hosts and collect all of their upti􏰁mes. We can’t simply map over hostnames because the result
   * a List[Future[Int]] would contain more than one Future.
   *
   * We need to reduce it to single future.
   */
  // Reducing to single Future with fold.
  val allUptimes: Future[List[Int]] = hostnames.foldLeft(Future(List.empty[Int])) {
    (accum, host) =>
      val uptime = getUptime(host)
      for {
        accum <- accum
        uptime <- uptime
      } yield accum :+ uptime
  }
  println(Await.result(allUptimes, 1.second))

  /**
   * Intui􏰁vely, we iterate over hostnames, call func for each item, and combine the results into a list.
   * This sounds simple, but the code is fairly unwieldy be- cause of the need to create and combine Futures at every itera􏰁on.
   * We can improve on things greatly using Future.traverse,
   */

  val allUptimes2: Future[List[Int]] =
    Future.traverse(hostnames)(getUptime)

  println(Await.result(allUptimes2, 1.second))

  /**
   * Future.traverse implementation in standard library is as below:
   *
   * def traverse[A, B](values: List[A])(func: A => Future[B]): Future[List[B]] =
   *     values.foldLeft(Future(List.empty[A])) { (accum, host) => val item = func(host)
   *     for {
   *       accum <- accum
   *       item  <- item
   *     } yield accum :+ item
   * }
   *
   * The standard library also provides another method, Future.sequence,
   * that assumes we’re starting with a List[Future[B]] and don’t need to provide an identity function:
   *
   * def sequence[B](futures: List[Future[B]]): Future[List[B]] =
   *     traverse(futures)(identity)
   *
   *
   * Cats’ Traverse type class Generalising these patterns to work with any type of Applicative:
   * Future, Option, Validated, and so on
   */

  import cats.Applicative
  import cats.syntax.apply._
  import cats.instances.list._
  import cats.syntax.applicative._

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) {
      (accum, item) => (accum, func(item)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  // So now the allUptimes can be reWritten as below:

  import cats.instances.future._
  val totalUptime = listTraverse(hostnames)(getUptime)
  println(Await.result(totalUptime, 1.second))

  // For traversing with Vector
  import cats.instances.vector._
  val value = listSequence(List(Vector(1, 2), Vector(3, 4)))
  println(value)

  println(listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6))))

  import cats.instances.option._
  def process(inputs: List[Int]) =
    listTraverse(inputs)(n => if (n % 2 == 0) Some(n) else None)

  println(process(List(1, 2, 3)))
  println(process(List(2, 4, 6)))

  import cats.data.Validated
  import cats.instances.list._ // for Monoid
  type ErrorsOr[A] = Validated[List[String], A]
  def processError(inputs: List[Int]): ErrorsOr[List[Int]] = listTraverse(inputs) { n =>
    if (n % 2 == 0) {
      Validated.valid(n)
    } else {
      Validated.invalid(List(s"$n is not even"))
    }
  }

  println(processError(List(1, 2, 3)))
  println(processError(List(2, 4, 6)))
  
  val numbers = List(Future(1), Future(2), Future(3))
  
  println(Await.result(listSequence(numbers), 1.second))

  
}
