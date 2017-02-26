name := "romesh-rest"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.90"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-sns" % "1.11.90"
libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.5.0"
libraryDependencies += "io.jsonwebtoken" % "jjwt" % "0.7.0"

// Test Dependencies

libraryDependencies += "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % "test"
libraryDependencies += "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.5.0" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"
