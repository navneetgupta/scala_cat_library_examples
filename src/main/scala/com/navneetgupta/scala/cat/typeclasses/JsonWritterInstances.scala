package com.navneetgupta.scala.cat.typeclasses

/**
  * Type Class Instances
  *
  * The instances of a type class provide implementa􏰀ons for the types we care about,
  * including types from the Scala standard library and types from our do- main model.
  * In Scala we define instances by crea􏰀ng concrete implementa􏰀ons of the type class
  * and tagging them with the implicit keyword:
  */
object JsonWritterInstances {
  implicit val stringWritter: JsonWritter[String] =
    new JsonWritter[String] {
      override def write(value: String): Json = JsString(value)
    }

  implicit val personWritter: JsonWritter[Person] =
    new JsonWritter[Person] {
      override def write(value: Person): Json = JsObject(Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)))
    }
}
