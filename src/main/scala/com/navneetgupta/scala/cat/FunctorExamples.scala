package com.navneetgupta.scala.cat

import cats._

object FunctorExamples extends App {

  import cats.implicits._

  /*
   * A Functor is a ubiquitous type class involving types that have one "hole", i.e. types which have
   * the shape F[?], such as Option, List and Future. (This is in contrast to a type like Int which
   * has no hole, or Tuple2 which has two holes (Tuple2[?,?])).
   *
   * The Functor category involves a single operation, named map
   * def map[A, B](fa: F[A])(f: A => B): F[B]
   * */

  // Creating Functor instances which have map method is trival
  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B) = fa map f
  }

  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B) = fa map f
  }

  // Functor Instances can be created also for type who do not have map mehtod.
  //  implicit def function1Functor[In]: Functor[Function1[In, ?]] =
  //    new Functor[Function1[In, ?]] {
  //      def map[A, B](fa: In => A)(f: A => B): Function1[In, B] = fa andThen f
  //    }

  println(Functor[Option].map(Option("Hello"))(_.length))
  println(Functor[Option].map(None: Option[String])(_.length))

  //We can use Functor to "lift" a function from A => B to F[A] => F[B]:
  val lenOption: Option[String] => Option[Int] = Functor[Option].lift(_.length)
  println(lenOption(Some("abcd")))

  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length).toMap

  println(product.get("Cats").getOrElse(0))
  println(product.get("is").getOrElse(0))
  println(product.get("awesome").getOrElse(0))
  println(product.get("First").getOrElse(0))

  /*
   * compose
   *     Functors compose! Given any functor F[_] and any functor G[_] we can create a new functor F[G[_]] by composing them
   *     val listOpt = Functor[List] compose Functor[Option]
   * */

  val listOpt = Functor[List] compose Functor[Option]
  println(listOpt.map(List(Some(1), None, Some(3)))(_ + 1))
}
