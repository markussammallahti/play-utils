name := "play-utils"

organization := "mrks"

licenses += ("Apache-2.0", url("https://github.com/markussammallahti/play-utils/blob/master/LICENSE"))

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.12.10", "2.13.1")

scalacOptions ++= Seq(
  "-encoding", "utf-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xfatal-warnings",
)
javacOptions ++= Seq(
  "-source", "8",
  "-target", "8"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.7.+" % Provided,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.+" % Provided,
  "commons-codec" % "commons-codec" % "1.14"
)
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test
)
