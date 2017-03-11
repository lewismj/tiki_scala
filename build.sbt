import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._


lazy val commonScalacOptions = Seq(
  "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xcheckinit",
  "-Xfuture",
  "-Xlint",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Yno-imports",
  "-Yno-predef")


lazy val buildSettings = Seq(
  name := "tiki",
  organization in Global := "com.waioeka",
  scalaVersion in Global := "2.12.1"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val scoverageSettings = Seq(
  coverageMinimum := 75,
  coverageFailOnMinimum := true
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.2"
  ),
  fork in test := true
)

lazy val tikiSettings = buildSettings ++ commonSettings ++ scoverageSettings

lazy val tiki = project.in(file("."))
  .settings(moduleName := "root")
  .settings(noPublishSettings)
  .aggregate(core, docs, tests)

lazy val core = project.in(file("core"))
  .settings(moduleName := "tiki-core")
  .settings(tikiSettings:_*)

lazy val docs = project
    .dependsOn(core)
    .settings(tikiSettings:_*)
    .settings(noPublishSettings)

lazy val tests = project.in(file("tests"))
  .dependsOn(core)
  .settings(moduleName := "tiki-tests")
  .settings(tikiSettings:_*)
  .settings(
    coverageEnabled := false,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-laws" % "0.9.0",
      "org.scalatest"  %% "scalatest" % "3.0.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )

