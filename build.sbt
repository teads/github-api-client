organization    := "tv.teads"
name            := "github-api-client"
scalaVersion    := "2.11.8"
scalacOptions   := Seq("-feature", "-deprecation", "-Xlint")

val circeVersion = "0.5.4"

libraryDependencies += "io.circe"                   %% "circe-core"       % circeVersion
libraryDependencies += "io.circe"                   %% "circe-generic"    % circeVersion
libraryDependencies += "io.circe"                   %% "circe-parser"       % circeVersion
libraryDependencies += "com.squareup.okhttp3"        % "okhttp"           % "3.4.1"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"    % "3.4.0"

libraryDependencies += "org.scalatest"     %% "scalatest"       % "2.2.6"     % "test"
libraryDependencies += "ch.qos.logback"     % "logback-classic" % "1.1.7"    % "test"

// Release Settings

def teadsRepo(repo: String) = repo at s"http://nexus.teads.net/nexus/content/repositories/$repo"

publishMavenStyle     := true
pomIncludeRepository  := { _ => false }
publishTo             := Some(if(isSnapshot.value) teadsRepo("snapshots") else teadsRepo("releases"))

credentials           += Credentials(Path.userHome / ".ivy2" / ".credentials")


