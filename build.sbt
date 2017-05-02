import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import UnidocKeys._
import com.typesafe.sbt.pgp.PgpKeys._


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
  "-Ywarn-value-discard")

lazy val buildSettings = Seq(
  name := "tiki",
  organization in Global := "com.waioeka",
  scalaVersion in Global := "2.12.1"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false,
  publishSigned := ()
)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  homepage := Some(url("https://github.com/lewismj/tiki")),
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  scmInfo := Some(ScmInfo(url("https://github.com/lewismj/tiki"), "scm:git:git@github.com:lewismj/tiki.git")),
  autoAPIMappings := true,
  pomExtra := (
    <developers>
      <developer>
        <name>Michael Lewis</name>
        <url>https://www.waioeka.com</url>
      </developer>
    </developers>
  )
) ++ credentialSettings

lazy val scoverageSettings = Seq(
  coverageMinimum := 75,
  coverageFailOnMinimum := false,
  coverageExcludedPackages := "instances"
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats" % "0.9.0",
    "org.scalanlp" %% "breeze" % "0.13.1",
    "org.scalanlp" %% "breeze-natives" % "0.13.1"
  ),
  fork in test := true
)

lazy val tikiSettings = buildSettings ++ commonSettings ++ scoverageSettings

lazy val tiki = project.in(file("."))
  .settings(moduleName := "root")
  .settings(noPublishSettings:_*)
  .aggregate(docs, tests, core)

lazy val core = project.in(file("core"))
  .settings(moduleName := "tiki-core")
  .settings(tikiSettings:_*)
  .settings(publishSettings:_*)

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
    "brand-primary" -> "#5B5988",
    "brand-secondary" -> "#292E53",
    "brand-tertiary" -> "#222749",
    "gray-dark" -> "#49494B",
    "gray" -> "#7B7B7E",
    "gray-light" -> "#E5E5E6",
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
    .settings(docSettings:_*)
    .settings(tikiSettings:_*)
    .settings(noPublishSettings:_*)

lazy val tests = project.in(file("tests"))
  .dependsOn(core)
  .settings(moduleName := "tiki-tests")
  .settings(tikiSettings:_*)
  .settings(noPublishSettings:_*)
  .settings(
    coverageEnabled := false,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-laws" % "0.9.0",
      "org.scalatest"  %% "scalatest" % "3.0.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )

