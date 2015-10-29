
name := "scalajs-offline"

version := "1.0"

scalaVersion := "2.11.7"

lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin)

//scala
val scalajsReactVersion = "0.8.2"
val scalajsPouchDBVersion = "0.1.0"
val scalajsDomVersion = "0.8.1"

//js
val reactVersion = "0.12.1"
val pouchDBVersion = "3.3.0"

// scalajs deps
libraryDependencies ++= Seq(
  "com.github.chandu0101.scalajs" %%% "pouchdb" % scalajsPouchDBVersion,
  "org.scala-js" %%% "scalajs-dom" % scalajsDomVersion,
  "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion,
  "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion
)

resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"

// js deps
jsDependencies ++= Seq(
  "org.webjars" % "react" % reactVersion / "react-with-addons.js" commonJSName "React",
  "org.webjars" % "pouchdb" % pouchDBVersion / "pouchdb.min.js")

// creates single jsdeps resource file for easy integration in html page
skip in packageJSDependencies := false

// uTest settings
//utest.jsrunner.Plugin.utestJsSettings


persistLauncher := true

persistLauncher in Test := false


// copy  javascript files to js folder,that are generated using fastOptJS/fullOptJS

crossTarget in(Compile, fullOptJS) := file("js")

crossTarget in(Compile, fastOptJS) := file("js")

crossTarget in(Compile, packageJSDependencies) := file("js")


//crossTarget in (Compile, packageScalaJSLauncher) := file("js")

artifactPath in(Compile, fastOptJS) := ((crossTarget in(Compile, fastOptJS)).value /
  ((moduleName in fastOptJS).value + "-opt.js"))

scalacOptions += "-feature"



