name := """boardcola"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,

  // Testing
  "org.scalatest"	%% "scalatest"		% "2.2.1" % "test",
  "org.scalatestplus"	%% "play"		% "1.4.0-M3" % "test",

  // Database dependencies
  "org.postgresql"	%  "postgresql"		% "9.4-1202-jdbc42",
  "com.typesafe.slick"	%% "slick"		% "3.0.2",
  "org.slf4j"		%  "slf4j-nop"		% "1.6.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
