package com.navneetgupta.scala.cat.typeclasses

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

/**
  * Type Class
  *
  * A type class is an interface or API that represents some func􏰀onality we want to implement.
  */
trait JsonWritter[A] {
  def write(value: A): Json
}

/**
  * Type Class Interfaces
  *
  * A type class interface is any func􏰀onality we expose to users.
  * Interfaces are generic methods that accept instances of the type class as implicit parameters.
  *
  * There are two common ways of specifying an interface:
  * Interface Objects and
  * Interface Syntax.
  */

/**
  * Interface Objects
  *
  * The simplest way of crea􏰀ting an interface is to place methods in a singleton object:
  *
  * To use this object, we import any type class instances we care about and call
  *
  * Uses::
  *
  * Json.toJson(Person("Navneet", "navneet@navneet.com")) is converted as by compiler
  * Json.toJson(Person("Navneet", "navneet@navneet.com"))(personWriter)
  */
object Json {
  def toJson[A](value: A)(implicit w: JsonWritter[A]): Json = w.write(value)
}

/**
  * Interface Syntax.
  *
  * We can alterna􏰀vely use extension methods to extend exis􏰀ng types with in- terface methods
  *
  * Uses:
  *
  * Person("Navneet", "navneet@navneet.com"").toJson
  *
  * For compiler it is Person("Navneet", "navneet@navneet.com").toJson(personWriter)
  *
  */
object JsonSyntax {

  implicit class JsonWritterOps[A](value: A) {
    def toJson(implicit w: JsonWritter[A]): Json =
      w.write(value)
  }

}
