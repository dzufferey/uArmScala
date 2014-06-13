name := "uArmScala"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.1"

scalacOptions in Compile ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++=  Seq(
    "org.scream3r" % "jssc" % "2.8.0"
)
