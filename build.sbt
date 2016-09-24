lazy val commonSettings = Seq(
  organization := "mm2slack",
  version := "0.1",

  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-encoding", "UTF-8",

    "-feature",
    "-unchecked",
    "-deprecation",

    "-language:postfixOps",
    "-language:implicitConversions",

    "-Ywarn-unused-import",
    "-Xfatal-warnings",
    "-Xlint:_"
  ),

  libraryDependencies ++= {
    val akkaVersion = "2.4.9"
    val scalatestVersion = "3.0.0"

    Seq(
      "com.github.jw3" %% "mm4s-api" % "0.2.2",
      "com.github.jw3" %% "mm4s-bots" % "0.2.2",
      "com.github.jw3" %% "mm4s-dockerbots" % "0.2.2",

      "com.iheart" %% "ficus" % "1.2.6",
      "com.elderresearch" %% "ssc" % "0.2.0",

      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-core" % akkaVersion,

      "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,

      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",

      "org.scalactic" %% "scalactic" % scalatestVersion % Test,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test
    )
  }
)


lazy val mm2slack =
  project.in(file("."))
  .aggregate(core)
  .settings(commonSettings: _*)


lazy val core =
  project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(name := "mm2slack-core")
