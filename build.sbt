import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import UnidocKeys._

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



lazy val docSettings = Seq(
  autoAPIMappings := true,
  micrositeName := "tiki",
  micrositeDescription := "Scala graph algorithms",
  micrositeBaseUrl :="/tiki",
  micrositeDocumentationUrl := "/tiki/api",
  micrositeGithubOwner := "lewismj",
  micrositeGithubRepo := "tiki",
  micrositeHighlightTheme := "atom-one-light",
  micrositePalette := Map(
    "brand-primary" -> "#eeeeee",
    "brand-secondary" -> "#4c4d4c",
    "brand-tertiary" -> "#5d5e5d",
    "gray-dark" -> "#48494B",
    "gray" -> "#7D7E7D",
    "gray-light" -> "#E5E6E5",
    "gray-lighter" -> "#F4F3F4",
    "white-color" -> "#FFFFFF"),
  git.remoteRepo := "git@github.com:lewismj/tiki.git",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md",
  ghpagesNoJekyll := false,
  siteSubdirName in ScalaUnidoc := "api",
  addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(tests)
)

lazy val docs = project
    .enablePlugins(MicrositesPlugin)
    .settings(moduleName := "tiki-docs")
    .settings(unidocSettings: _*)
    .settings(ghpages.settings)
    .dependsOn(core)
    .settings(docSettings)
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

