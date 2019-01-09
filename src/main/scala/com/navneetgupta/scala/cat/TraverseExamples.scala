package com.navneetgupta.scala.cat

import cats._

object TraverseExamples extends App {
	import cats.implicits._
	import cats.Semigroup
	import cats.data.{ NonEmptyList, OneAnd, Validated, ValidatedNel }

	/**
	 *   trait Traverse[F[_]] {
	 * 		def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
	 * 	 }
	 * In our above example, F is List, and G is Option, Either, or Future. For the profile example,
	 * traverse says given a List[User] and a function User => Future[Profile], it can give you a
	 * Future[List[Profile]]
	 */

	def parseIntEither(s: String): Either[NumberFormatException, Int] =
		Either.catchOnly[NumberFormatException](s.toInt)

	def parseIntValidated(s: String): ValidatedNel[NumberFormatException, Int] =
		Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel

	println(List("1", "2", "3").traverse(parseIntEither)) // Right(List(1,2,3))
	println(List("1", "abc", "3").traverse(parseIntEither).isLeft) // true

	//	implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = OneAnd.oneAndSemigroupK[List].algebra[A]

	println(List("1", "2", "3").traverse(parseIntValidated).isValid) //true

	println(List(Option(1), Option(2), Option(3)).traverse(identity)) // Some(List(1,2,3))

	println(List(Option(1), None, Option(3)).traverse(identity)) // None

	println(List(Option(1), Option(2), Option(3)).sequence) // Some(List(1,2,3))

	println(List(Option(1), None, Option(3)).sequence) // None

	println(List(Option(1), Option(2), Option(3)).sequence_) // Some(())

	println(List(Option(1), None, Option(3)).sequence_) // None

}
