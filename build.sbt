name := "seqa"

version := "1.0"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.3.1" % "provided",
	"org.ini4j" % "ini4j" % "0.5.4",
	"com.typesafe" % "config" % "1.3.0",
	"it.unimi.dsi" % "fastutil" % "7.0.4",
	"org.scalanlp" %% "breeze" % "0.11.2"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyJarName in assembly := "SeqA-1.0.jar"
