name := "LocaQuest"
 
scalaVersion := "2.9.1"

scalacOptions := "-deprecation" :: "-unchecked" :: Nil

seq(webSettings :_*)

resolvers ++= Seq(
"Scala Tools Releases" at "http://scala-tools.org/repo-releases/",
"Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/",
"Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
"Jetty Repo" at "http://repo1.maven.org/maven2/org/mortbay/jetty/")


libraryDependencies ++= {
val liftVersion = "2.4"
Seq(
"org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container",
"net.liftweb" %% "lift-webkit" % liftVersion,
"net.liftweb" %% "lift-mapper" % liftVersion,
"net.liftweb" %% "lift-wizard" % liftVersion,
"net.liftweb" %% "lift-mongodb" % liftVersion,
"net.liftweb" %% "lift-mongodb-record" % liftVersion,
"net.liftweb" %% "lift-widgets" % liftVersion,
"joda-time" % "joda-time" % "2.0",
"org.joda" % "joda-convert" % "1.2",
"com.foursquare" %% "rogue" % "1.1.0" intransitive(),
"com.mongodb.casbah" % "casbah_2.9.1" % "2.1.5-1",
"commons-httpclient" % "commons-httpclient" % "3.1",
"ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default"
)
}

