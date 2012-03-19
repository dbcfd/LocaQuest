import sbt._
import com.github.siasia._
import WebPlugin._
import PluginKeys._
import Keys._

object Build extends sbt.Build {
  import Dependencies._

  lazy val myProject = Project("Locaquest", file("."))
    .settings(WebPlugin.webSettings: _*)
    .settings(port in config("container") := 8080)
    .settings(
      organization  := "com.webwino",
      version       := "0.2.0",
      scalaVersion  := "2.9.1",
      scalacOptions := Seq("-deprecation", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      libraryDependencies ++= Seq(
        Compile.liftWebkit,
        Compile.liftMapper,
        Compile.liftWizard,
        Compile.liftWidgets,
        Test.specs2,
        Container.jettyWebApp,
        Container.logback
      )
    )
}

object Dependencies {
  val resolutionRepos = Seq(
    ScalaToolsSnapshots,
    "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
    "spray repo" at "http://repo.spray.cc/"
  )

  object V {
    val liftVersion = "2.4"
    val jetty   = "8.1.0.v20120127"
    val logback = "1.0.0"
    val specs2  = "1.7.1"
  }

  object Compile {
    val liftWebkit  = "net.liftweb"               %% "lift-webkit"     % V.liftVersion % "compile"
    val liftMapper  = "net.liftweb"               %% "lift-mapper"     % V.liftVersion % "compile"
    val liftWizard  = "net.liftweb"               %% "lift-wizard"     % V.liftVersion % "compile"
    val liftWidgets = "net.liftweb"               %% "lift-widgets"    % V.liftVersion % "compile"
  }

  object Test {
    val specs2      = "org.specs2"                %% "specs2"          % V.specs2  % "test"
  }

  object Container {
    val jettyWebApp = "org.eclipse.jetty"         %  "jetty-webapp"    % V.jetty   % "container"
    val logback     = "ch.qos.logback"            %  "logback-classic" % V.logback
  }
}
