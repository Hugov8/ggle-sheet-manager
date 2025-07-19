name := """sheetUpdateService"""
organization := "fr.hugov"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

Docker / maintainer := "hugovedrine@hotmail.com"
Docker / packageName := "ggle-sheet-manager"
Docker / version := "1.0"
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts := Seq(9900)
dockerBaseImage := "openjdk:11-jre-slim"
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true
ThisBuild / dockerEnvVars += ("TOKEN_API" -> sys.env.getOrElse("TOKEN_API", ""))


scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "fr.hugov.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "fr.hugov.binders._"

//Request dependencies
libraryDependencies += "io.lemonlabs" %% "scala-uri" % "4.0.3"

//Google sheet dependencies
libraryDependencies += "com.google.api-client" % "google-api-client" % "2.0.0"
libraryDependencies += "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1"
libraryDependencies += "com.google.http-client" % "google-http-client-jackson2" % "1.43.0"
libraryDependencies += "com.google.apis" % "google-api-services-sheets" % "v4-rev20220927-2.0.0"
libraryDependencies += "com.google.auth" % "google-auth-library-credentials" % "1.11.0"
libraryDependencies += "com.google.auth" % "google-auth-library-oauth2-http" % "1.11.0"
//Google drive dependencies
libraryDependencies += "com.google.apis" % "google-api-services-drive" % "v3-rev20220815-2.0.0"
