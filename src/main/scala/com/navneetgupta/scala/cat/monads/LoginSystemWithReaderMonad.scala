package com.navneetgupta.scala.cat.monads

import cats._
import cats.implicits._
import cats.data.Reader

object LoginSystemWithReaderMonad extends App {
  case class Db(
    username: Map[Int, String],
    password: Map[String, String])

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] = Reader(db => db.username.get(userId))

  def checkPassword(
    username: String,
    password: String): DbReader[Boolean] =
    Reader(db => db.password.get(username).contains(password))

  def checkLogin(
    userId: Int,
    password: String): DbReader[Boolean] =
    for {
      usernameOpt <- findUsername(userId)
      isValid <- usernameOpt.map { username => checkPassword(username, password) }.getOrElse { false.pure[DbReader] }
    } yield isValid

  val usernameMap = Map(1 -> "One", 2 -> "Two", 3 -> "Three", 4 -> "Four")
  val passwordMap = Map("One" -> "password", "Two" -> "password", "Three" -> "password", "Four" -> "password")
  val db = Db(usernameMap, passwordMap)
  println(checkLogin(3, "password")(db))

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo")
  val passwords = Map(
    "dade" -> "zerocool",
    "kate" -> "acidburn",
    "margo" -> "secret")

  val db1 = Db(users, passwords)
  println(checkLogin(1, "zerocool").run(db1))
  // res10: cats.Id[Boolean] = true
  println(checkLogin(4, "davinci").run(db1))
}
