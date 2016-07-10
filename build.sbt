version := "1.0"
scalaVersion := "2.11.8"
name := "phantom-twitter"

val PhantomVersion = "1.22.0"
val sparkVersion = "1.6.2"
val akkaVersion = "2.4.7"

resolvers ++= Seq(
  "spray repo"                       at "http://repo.spray.io",
  "Typesafe repository snapshots"    at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo"                    at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases"                at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots"               at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging"                 at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  "Spark Packages Repo"              at "http://dl.bintray.com/spark-packages/maven",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++= Seq(
  "com.websudos" %% "phantom-dsl" % PhantomVersion,
  "org.twitter4j" % "twitter4j-stream" % "4.0.4",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M3",
  "com.typesafe.play" %% "play-json" % "2.5.4" exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.kafka" %% "kafka" % "0.8.2.1",
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "com.ibm.watson.developer_cloud" % "java-sdk" % "3.0.1"
).map(
  _.excludeAll(
    ExclusionRule(organization = "org.mortbay.jetty")
  )
)
