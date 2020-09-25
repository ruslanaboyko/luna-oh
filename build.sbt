name := "luna-oh"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.javacord" % "javacord" % "3.0.6",
  "com.github.pathikrit" %% "better-files" % "3.9.1",
  "com.typesafe.play" %% "play-json" % "2.9.0",
  "org.scalactic" %% "scalactic" % "3.2.0",
  "org.scalatest" %% "scalatest" % "3.2.0" % "test"
)