name := """leadboxer-notification"""

version := "0.0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies += javaJpa

libraryDependencies += jdbc

libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0"

libraryDependencies += javaWs

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.5.Final"

libraryDependencies += "org.webjars" % "jquery" % "2.1.1"

//libraryDependencies += "org.webjars" % "bootstrap" % "3.3.1"

libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"

// see https://github.com/playframework/play-mailer
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "5.0.0"


// Compile the project before generating Eclipse files, so that generated .scala
// or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

// https://www.playframework.com/documentation/2.5.x/IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes
EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)
EclipseKeys.withSource := true
