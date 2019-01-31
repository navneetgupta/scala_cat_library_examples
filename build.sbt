

name := "scala_cat_library_examples"

organization := "com.navneetgupta"

scalaVersion := "2.12.6"

version      := "0.1.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-language:higherKinds"
)

libraryDependencies ++= {
  val catVersion = "1.5.0"
  Seq(
    "org.typelevel" %% "cats-core" % catVersion,
    "org.typelevel" %% "cats-macros" % catVersion,
    "org.typelevel" %% "cats-kernel" % catVersion,
    "org.typelevel" %% "cats-laws" % catVersion,
    "org.typelevel" %% "cats-free" % catVersion,
    "org.typelevel" %% "cats-free" % catVersion,
    "org.typelevel" %% "cats-effect" % "1.2.0"
  )
}