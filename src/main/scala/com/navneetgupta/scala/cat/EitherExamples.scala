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

	/**
	 * Once you start using Either for all your error-handling, you may quickly run into an issue where you need to call into two separate modules which give back separate kinds of errors
	 *
	 * for Example :
	 * an application that wants to do database things, and then take database values and do service things. Glancing at the types, it looks like flatMap will do it.
	 * 			def doApp = Database.databaseThings().flatMap(Service.serviceThings)
	 * This doesn't work! Well, it does, but it gives us Either[Object, ServiceValue] which isn't particularly useful for us
	 *
	 *  the type parameters of Either are covariant, so when it sees an Either[E1, A1] and an Either[E2, A2], it will happily try to unify the E1 and E2 in a flatMap call -
	 *  in our case, the closest common supertype is Object, leaving us with practically no type information to use in our pattern match.
	 */

	//Solution 1: Application-wide errors
	object Demo1 {
		sealed abstract class AppError
		final case object DatabaseError1 extends AppError
		final case object DatabaseError2 extends AppError
		final case object ServiceError1 extends AppError
		final case object ServiceError2 extends AppError

		trait DatabaseValue

		object Database {
			def databaseThings(): Either[AppError, DatabaseValue] = ???
		}

		trait ServiceValue

		object Service {
			def serviceThings(v: DatabaseValue): Either[AppError, ServiceValue] = ???
		}

		def doApp = Database.databaseThings().flatMap(Service.serviceThings)
	}

	/* Above works but if we need to use where we only need to get DatabaseError, and gets an Either[AppError,_] then we need to patternmatch every cases. or atlease with specific and wild cases*/

	/*
	 * Solution 2: ADTs all the way down
	 * 		Instead of lumping all our errors into one big ADT, we can instead keep them local to each module, and have an application-wide error ADT that wraps each error ADT we need.
	 */

	object Demo2 {
		sealed abstract class DatabaseError
		trait DatabaseValue

		object Database {
			def databaseThings(): Either[DatabaseError, DatabaseValue] = ???
		}

		sealed abstract class ServiceError
		trait ServiceValue

		object Service {
			def serviceThings(v: DatabaseValue): Either[ServiceError, ServiceValue] = ???
		}

		sealed abstract class AppError
		object AppError {
			final case class Database(error: DatabaseError) extends AppError
			final case class Service(error: ServiceError) extends AppError
		}

		def doApp: Either[AppError, ServiceValue] =
			Database.databaseThings().leftMap(AppError.Database).
				flatMap(dv => Service.serviceThings(dv).leftMap(AppError.Service))

		def awesome =
			doApp match {
				case Left(AppError.Database(_)) => "something in the database went wrong"
				case Left(AppError.Service(_)) => "something in the service went wrong"
				case Right(_) => "everything is alright!"
			}
	}

	val either: Either[NumberFormatException, Int] =
		Either.catchOnly[NumberFormatException]("abc".toInt)

	val right2: Either[String, Int] = Right(41)
	println(right2.map(_ + 1)) // Right(42)

	val left2: Either[String, Int] = Left("Hello")
	println(left2.map(_ + 1)) // Left("Hello")
	println(left2.leftMap(_.reverse)) // Left("olleH")

	println(Either.catchOnly[NumberFormatException]("abc".toInt).isRight) // false
	println(Either.catchNonFatal(1 / 0).isLeft) // true

	val right3: Either[String, Int] = 42.asRight[String]
	println(right3) // Right(42)
}
