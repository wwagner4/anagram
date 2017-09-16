import sbt.ExclusionRulelazy val dl4jVersion = "0.8.0"name := "anagram"version := "0.1"scalaVersion := "2.12.3"libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion excludeAll(  ExclusionRule(organization = "com.google.guava"),  ExclusionRule(organization = "org.slf4j"),  ExclusionRule(organization = "com.google.code.findbugs"))libraryDependencies += "org.nd4j" % "nd4j-native-platform" % dl4jVersionlibraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"libraryDependencies += "junit" % "junit" % "4.12" % "test"