//Revolver.settings

scalaVersion := "2.11.6"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.sonatypeRepo("releases")
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
//resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"


// Typesafe Config and friends
libraryDependencies += "net.ceedubs" %% "ficus" % "1.1.2"
libraryDependencies += "io.spray" %% "spray-client" % "1.3.2"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.9"
libraryDependencies += "io.github.jto" %% "validation-core" % "1.0.1"
libraryDependencies += "io.github.jto" %% "validation-json" % "1.0.1"
libraryDependencies += "org.yaml" % "snakeyaml" % "1.15"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"
