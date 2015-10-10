import com.tuplejump.sbt.yeoman.Yeoman

name := """boardcola"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Atlassian Releases" at "https://maven.atlassian.com/public/"
)

// referenced for help:
// https://github.com/mohiva/play-silhouette-angular-seed
libraryDependencies ++= Seq(
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
  //"com.typesafe.slick"  %% "slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",

  // Dependency injection
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "net.ceedubs" %% "ficus" % "1.1.2",

  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

Yeoman.yeomanSettings ++ Yeoman.withTemplates