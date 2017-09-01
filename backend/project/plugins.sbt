// Scalastyle plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

// Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.3")

// Scala linter
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.1.1")
addSbtPlugin("org.danielnixon" % "sbt-extrawarts" % "0.3.0")
addSbtPlugin("org.danielnixon" % "sbt-playwarts" % "1.0.0")

// Dependency Loader
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC10")

// cucumber
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.1.4")

// Deployment
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.5")
libraryDependencies += "com.spotify" % "docker-client" % "8.0.0"
