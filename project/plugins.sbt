// +------------------------------------------------------------------------------------+
// | SBT Eclipse (https://github.com/typesafehub/sbteclipse) |
// | Creates .project and .classpath files for easy Eclipse project imports |
// | |
// | See also: Eclipse downloads (http://www.eclipse.org/downloads/) |
// | See also: Scala IDE downloads (http://download.scala-ide.org/) |
// +------------------------------------------------------------------------------------+

resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

// +-------------------------------------------------------------------------------------+
// | XSBT Web plugin (https://github.com/siasia/xsbt-web-plugin) |
// | Implements SBT 0.7.x Web project actions: "jetty-run" -> "container:start", etc ... |
// +-------------------------------------------------------------------------------------+
libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.11"))
