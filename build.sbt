scalaVersion := "2.11.6"

resolvers += Opts.resolver.sonatypeReleases

libraryDependencies += "com.typesafe.akka" %% "akka-actor"      % "2.3.11" % "provided"
libraryDependencies += "net.ceedubs"       %% "ficus"           % "1.1.2"
libraryDependencies += "io.spray"          %% "spray-client"    % "1.3.2"
libraryDependencies += "com.typesafe.play" %% "play-json"       % "2.3.9"
libraryDependencies += "io.github.jto"     %% "validation-core" % "1.0.1"
libraryDependencies += "io.github.jto"     %% "validation-json" % "1.0.1"
libraryDependencies += "ch.qos.logback"     % "logback-classic" % "1.0.13"

libraryDependencies += "org.scalatest"     %% "scalatest"       % "2.2.5"  % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit"    % "2.3.11" % "test"
