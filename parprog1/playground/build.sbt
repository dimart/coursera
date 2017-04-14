name := "playground"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"

// scalameter-core module for lightweight inline benchmarking
libraryDependencies +=
  "com.storm-enroute" %% "scalameter-core" % "0.8.2"