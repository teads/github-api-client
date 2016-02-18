name            := "github-api-client"
organization    := "tv.teads"
scalaVersion    := "2.11.7"
scalacOptions   := Seq("-feature", "-deprecation", "-Xlint")

val circeVersion = "0.3.0"

libraryDependencies += "io.circe"                   %% "circe-core"       % circeVersion
libraryDependencies += "io.circe"                   %% "circe-generic"    % circeVersion
libraryDependencies += "io.circe"                   %% "circe-jawn"       % circeVersion
libraryDependencies += "com.squareup.okhttp3"        % "okhttp"           % "3.1.2"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"    % "3.1.0"

libraryDependencies += "org.scalatest"     %% "scalatest"       % "2.2.5"     % "test"
libraryDependencies += "ch.qos.logback"     % "logback-classic" % "1.0.13"    % "test"

// Release Settings

def teadsRepo(repo: String) = repo at s"http://nexus.teads.net/nexus/content/repositories/$repo"

publishMavenStyle     := true
pomIncludeRepository  := { _ => false }
publishTo             := Some(if(isSnapshot.value) teadsRepo("snapshots") else teadsRepo("releases"))

credentials           += Credentials(Path.userHome / ".ivy2" / ".credentials")


