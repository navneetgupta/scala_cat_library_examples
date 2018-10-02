package com.navneetgupta.scala.cat.functors

import cats.Contravariant
import cats.Show
import cats.instances.string._

object CatsContravariantEx extends App {

  val showString = Show[String]

  val showSymbol = Contravariant[Show].
    contramap(showString)((sym: Symbol) => s"'${sym.name}")

  println(showSymbol.show('dave))

  import cats.syntax.contravariant._

  println(showString.contramap[Symbol](_.name).show('dave))

}
