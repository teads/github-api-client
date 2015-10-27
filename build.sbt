name            := "github-api-client"
organization    := "tv.teads"
scalaVersion    := "2.11.7"
scalacOptions   := Seq("-feature", "-deprecation", "-Xlint")

resolvers += Opts.resolver.sonatypeReleases

val akkaVersion = "2.3.13"
val circeVersion = "0.1.1"

libraryDependencies += "com.typesafe.akka"          %% "akka-actor"       % akkaVersion % "provided"
libraryDependencies += "io.circe"                   %% "circe-core"       % circeVersion
libraryDependencies += "io.circe"                   %% "circe-generic"    % circeVersion
libraryDependencies += "io.circe"                   %% "circe-jawn"       % circeVersion
libraryDependencies += "io.spray"                   %%  "spray-client"    % "1.3.2"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"    % "3.1.0"
libraryDependencies += "com.chuusai"                %%  "shapeless"       % "2.2.5"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit"    % akkaVersion % "test"
libraryDependencies += "org.scalatest"     %% "scalatest"       % "2.2.5"     % "test"
libraryDependencies += "ch.qos.logback"     % "logback-classic" % "1.0.13"    % "test"

// Release Settings

def teadsRepo(repo: String) = repo at s"http://nexus.teads.net/nexus/content/repositories/$repo"

publishMavenStyle     := true
pomIncludeRepository  := { _ => false }
publishTo             := Some(if(isSnapshot.value) teadsRepo("snapshots") else teadsRepo("releases"))

credentials           += Credentials(Path.userHome / ".ivy2" / ".credentials")


