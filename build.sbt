name := "markov"

organization := "gen"

version := "0.0.1"

scalaVersion := "2.11.7"

scalacOptions in Test ++= Seq("-Yrangepos")

tutSettings

tutTargetDirectory <<= baseDirectory

libraryDependencies ++= Seq(
  "com.chuusai"    %% "shapeless"         % "2.2.5",
  "org.specs2"     %% "specs2-core"       % "3.6.6"  % "test"
)
