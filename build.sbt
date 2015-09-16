name := """boardcola"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Atlassian Releases" at "https://maven.atlassian.com/public/"
)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,

  // Testing
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",

  // Authentication
  "com.mohiva" %% "play-silhouette" % "3.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "3.0.0" % "test",

  // Database dependencies
  "org.postgresql" %  "postgresql" % "9.4-1202-jdbc42",
  "com.typesafe.slick"  %% "slick" % "3.0.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",

  "org.flywaydb" %% "flyway-play" % "2.2.0"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
