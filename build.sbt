import sbt.ExclusionRulelazy val dl4jVersion = "0.8.0"name := "anagram"version := "0.1-SNAPSHOT"scalaVersion := "2.12.4"val ivyLocal = Resolver.file("local", file("/Users/wwagner4/.ivy2/local"))(Resolver.ivyStylePatterns)externalResolvers += ivyLocallibraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion excludeAll(  ExclusionRule(organization = "org.slf4j"),  ExclusionRule(organization = "com.google.code.findbugs"))libraryDependencies += "org.nd4j" % "nd4j-native-platform" % dl4jVersionlibraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"libraryDependencies ++= Seq(  "com.typesafe.akka" %% "akka-actor" % "2.5.6",  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test)libraryDependencies += "com.twelvemonkeys.imageio" % "imageio-core" % "3.3.2"libraryDependencies += "com.miglayout" % "miglayout" % "3.7.4"libraryDependencies += "net.entelijan" %% "vis" % "0.1-SNAPSHOT"libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"libraryDependencies += "junit" % "junit" % "4.12" % "test"libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"