version := "1.0"
scalaVersion := "2.11.8"
name := "phantom-twitter"

val PhantomVersion = "1.22.0"
val sprayV = "1.3.2"

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
  Resolver.bintrayRepo("cakesolutions", "maven"),
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++= Seq(
  "com.websudos" %% "phantom-dsl" % PhantomVersion,
  "org.twitter4j" % "twitter4j-stream" % "4.0.4",
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.google.code.gson" % "gson" % "2.6.2",
  "net.cakesolutions" %% "scala-kafka-client-akka" % "0.7.0",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M3",
  "com.typesafe.play" % "play-json_2.11" % "2.5.4"
)
