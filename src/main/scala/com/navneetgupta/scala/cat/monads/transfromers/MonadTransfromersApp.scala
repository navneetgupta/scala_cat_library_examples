package com.navneetgupta.scala.cat.monads.transfromers

import cats._
import cats.implicits._
import scala.concurrent.Future
import cats.instances.future._ // for Monad
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object MonadTransfromersApp extends App {

  case class User(name: String, email: String, mobile: Option[String] = None)

  def loopkup(id: String): Either[Error, Option[String]] = {
    for {
      optUser <- getUserById(id)
    } yield {
      for {
        user <- optUser
      } yield {
        user.name
      }
    }
  }

  //  def compose[M1[_]: Monad, Option[_]: Monad] = {
  //    type Composed[A] = M1[Option[A]]
  //    new Monad[Composed] {
  //      def pure[A](a: A): Composed[A] =
  //        a.pure[Option].pure[M1]
  //      def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] =
  //        fa.flatMap(_.fold(None.pure[M])(f))
  //    }
  //  }

  //  def flatMap[A, B](fa: Composed[A])
  //    (f: A => Composed[B]): Composed[B] =
  //

  /**
   * The user may or may not be present, so we return an Option[User].
   * Our communication􏰀 with the database could fail for many reasons (network issues, authen􏰀ca􏰀on problems, and so on),
   * so this result is wrapped up in an Either, giving us a final result of Either[Error, Option[User]]
   */
  def getUserById(id: String): Either[Error, Option[User]] = ???

  /**
   * In above lookup Function we have to use flatmap (for-yield) combination
   * Multiple time to get the desired result.
   */

  /**
   * Can we Compose two or more monad?
   * Not In General But if we know about one or another Monad we can Compose.
   * This is where Monad Transformers Comes in place
   */

  import cats.data.OptionT

  type ListOption[A] = OptionT[List, A]

  val list1: ListOption[Int] = OptionT(List(Option(12), Option(2), Option(22)))
  val list2: ListOption[Int] = 32.pure[ListOption]

  val a = list1.flatMap { (x: Int) =>
    list2.map { (y: Int) =>
      (x + y)
    }
  }

  val a1 = list1.flatMap { (x: Int) =>
    list2.flatMap { (y: Int) =>
      (x + y).pure[ListOption]
    }
  }

  println(a.value)
  println(a1.value)

  //import cats.data.EitherT

  type EitherOr[A] = Either[String, A]
  type EitherOption[A] = OptionT[EitherOr, A]

  case class EitherT[F[_], E, A](stack: F[Either[E, A]])

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  //  val futureEitherOr: FutureEitherOption[Int] =
  //    for {
  //      a <- 10.pure[FutureEitherOption]
  //      b <- 32.pure[FutureEitherOption]
  //    } yield a + b
}
