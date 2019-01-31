package com.navneetgupta.scala.cat.typeclasses

import JsonWritterInstances._

object PersonApp extends App {
  println(Json.toJson(Person("Navneet", "navneet@navneet.com")))

  import JsonSyntax._

  println(Person("Navneet", "navneet@navneet.com").toJson)
}
