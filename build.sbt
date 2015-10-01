name            := "github-api-client"
organization    := "tv.teads"
scalaVersion    := "2.11.7"

resolvers += Opts.resolver.sonatypeReleases

val akkaVersion = "2.3.13"
val Akka = Seq(
  "com.typesafe.akka" %% "akka-actor"     % akkaVersion % "provided",
  "com.typesafe.akka" %% "akka-testkit"   % akkaVersion % "test"
)

libraryDependencies ++= Akka
// Typesafe Config and friends
libraryDependencies += "net.ceedubs"        %%  "ficus"           % "1.1.2"
libraryDependencies += "io.spray"           %%  "spray-client"    % "1.3.2"
libraryDependencies += "com.typesafe.play"  %%  "play-json"       % "2.3.9"
libraryDependencies += "io.github.jto"      %%  "validation-core" % "1.0.2"
libraryDependencies += "io.github.jto"      %%  "validation-json" % "1.0.2"
libraryDependencies += "org.yaml"           %   "snakeyaml"       % "1.15"
libraryDependencies += "ch.qos.logback"     %   "logback-classic" % "1.0.13"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "com.chuusai"        %%  "shapeless"       % "2.2.5"

libraryDependencies += "org.scalatest"      %% "scalatest"        % "2.2.5"   % "test"

// Release Settings

def teadsRepo(repo: String) =
  repo at s"http://nexus.teads.net/nexus/content/repositories/$repo"

publishMavenStyle     := true
pomIncludeRepository  := { _ => false }
publishTo             := Some(if(isSnapshot.value) teadsRepo("snapshots") else teadsRepo("releases"))

credentials           += Credentials(Path.userHome / ".ivy2" / ".credentials")


