name := """IT5100A"""
organization := "com.renren"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "org.xerial" % "sqlite-jdbc"% "3.7.2",
  "mysql" % "mysql-connector-java" % "8.0.15"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.renren.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.renren.binders._"
