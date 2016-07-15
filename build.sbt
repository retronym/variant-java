crossScalaVersions := Seq("2.12.0-M5")
scalaVersion       := crossScalaVersions.value.head
organization       := "com.github.retronym"
name               := "variant-java"  
version            := "0.1-SNAPSHOT"

publishArtifact in Test := false

publishTo := Some(
  if (version.value.trim.endsWith("SNAPSHOT")) Resolver.sonatypeRepo("snapshots")
  else Opts.resolver.sonatypeStaging
)
credentials ++= {
  val file = Path.userHome / ".ivy2" / ".credentials"
  if (file.exists) List(new FileCredentials(file)) else Nil
}

val repoName         = "variant-java"

publishMavenStyle    := true
scmInfo              := Some(ScmInfo(url(s"https://github.com/retronym/${repoName}"),s"scm:git:git://github.com/scala/${repoName}.git"))
licenses             := Seq("BSD 3-clause" -> url("http://opensource.org/licenses/BSD-3-Clause"))
startYear            := Some(2016)
pomIncludeRepository := { _ => false }

libraryDependencies += ("org.scala-lang" % "scala-compiler" % scalaVersion.value)

scalacOptions in Test ++= {
   val jar: File = (Keys.`package` in Compile).value
   val addPlugin = "-Xplugin:" + jar.getAbsolutePath
   // add plugin timestamp to compiler options to trigger recompile of
   // main after editing the plugin. (Otherwise a 'clean' is needed.)
   val dummy = "-Jdummy=" + jar.lastModified
   Seq(addPlugin, dummy)
}
