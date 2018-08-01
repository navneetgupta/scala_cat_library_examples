package com.navneetgupta.scala.cat
import cats._
object EitherExamples extends App {
	import cats.implicits._

	val right: Either[String, Int] = Either.right(5)
	println(right.map(_ + 1)) // Right(6)

	val left: Either[String, Int] = Either.left("Some Error Occured")
	println(left.map(_ + 1)) // Left("Some Error Occured")

	println(right.flatMap(x => Either.right(x + 1))) // Right(6)
	println(left.flatMap(x => Either.left(x + 1))) // Left("Some Error Occured")

	object ExceptionStyle {
		def parse(s: String): Int =
			if (s.matches("-?[0-9]+")) s.toInt
			else throw new NumberFormatException(s"${s} is not a valid integer.")

		def reciprocal(i: Int): Double =
			if (i == 0) throw new IllegalArgumentException("Cannot take reciprocal of 0.")
			else 1.0 / i

		def stringify(d: Double): String = d.toString

	}

	object EitherStyle {
		def parse(s: String): Either[NumberFormatException, Int] =
			if (s.matches("-?[0-9]+")) Either.right(s.toInt)
			else Either.left(new NumberFormatException(s"${s} is not a valid integer."))

		def reciprocal(i: Int): Either[IllegalArgumentException, Double] =
			if (i == 0) Either.left(new IllegalArgumentException("Cannot take reciprocal of 0."))
			else Either.right(1.0 / i)

		def stringify(d: Double): String = d.toString

		def magic(s: String): Either[Exception, String] =
			parse(s).flatMap(reciprocal).map(stringify)
	}

	println(EitherStyle.parse("Not a number").isRight) // false

	println(EitherStyle.parse("2").isRight) // true

	println(EitherStyle.magic("0").isRight) // false
	println(EitherStyle.magic("1").isRight) // true
	println(EitherStyle.magic("Not a number").isRight) // false

	val result = EitherStyle.magic("2") match {
		case Left(_: NumberFormatException) => "Not a Number"
		case Left(_: IllegalArgumentException) => "Can't make a reciprocal of 0 , 1"
		case Left(_) => "Unknown Exception"
		case Right(result) => s"Got reciprocal is : ${result}"
	}

	println(result) // Got reciprocal is : 0.5

	object EitherStyleWithAdts {
		sealed abstract class Error
		final case class NotANumber(string: String) extends Error
		final case object NoZeroReciprocal extends Error

		def parse(s: String): Either[Error, Int] =
			if (s.matches("-?[0-9]+")) Either.right(s.toInt)
			else Either.left(NotANumber(s))

		def reciprocal(i: Int): Either[Error, Double] =
			if (i == 0) Either.left(NoZeroReciprocal)
			else Either.right(1.0 / i)

		def stringify(d: Double): String = d.toString

		def magic(s: String): Either[Error, String] =
			parse(s).flatMap(reciprocal).map(stringify)
	}

	val result1 = EitherStyleWithAdts.magic("2") match {
		case Left(EitherStyleWithAdts.NotANumber(_)) ⇒ "Not a number!"
		case Left(EitherStyleWithAdts.NoZeroReciprocal) ⇒ "Can't take reciprocal of 0!"
		case Right(result) ⇒ s"Got reciprocal: ${result}"
	}
	println(result1) //Got reciprocal is : 0.5
}
