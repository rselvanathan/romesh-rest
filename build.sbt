name := "romesh-rest"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.90"

// Test Dependencies

libraryDependencies += "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % "test"