name := "nifi-template-bundle"
organization := "com.tgsight"
version := "0.1-SNAPSHOT"

scalaVersion := "2.13.5"
nifiVersion := "1.9.0"

resolvers += Resolver.bintrayRepo("palindromicity", "simple-syslog-5424")

enablePlugins(NarPlugin)

libraryDependencies ++= Seq(
  "org.apache.nifi" % "nifi-api",
  "org.apache.nifi" % "nifi-nar-bundles",
//  "org.apache.nifi" % "nifi-dbcp-service-api",
  "org.apache.nifi" % "nifi-record-serialization-service-api",
//  "org.apache.nifi" % "nifi-schema-registry-service-api",
  "org.apache.nifi" % "nifi-ssl-context-service-api",
  "org.apache.nifi" % "nifi-processor-utils",
//  "org.apache.nifi" % "nifi-standard-record-utils",
//  "org.apache.nifi" % "nifi-record"
).map(_ % nifiVersion.value)

libraryDependencies ++= Seq(
  "org.apache.nifi" % "nifi-record-serialization-services"
).map(_ % nifiVersion.value % "provided")

libraryDependencies ++= Seq(
  "org.apache.nifi"  % "nifi-record-serialization-services",
  "org.apache.nifi"  % "nifi-mock",
  "org.apache.nifi"  % "nifi-mock-record-utils",
  "org.apache.nifi"  % "nifi-framework-nar-utils"
).map(_ % nifiVersion.value % Test)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library"  % scalaVersion.value,
  "org.scalatest"  %% "scalatest"     % "3.0.8"      % Test
)

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding", "UTF-8",
  "-Xlint",
  "-Xverify",
  "-feature",
  "-language:_"
)
