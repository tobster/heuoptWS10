import sbt._

class MySbtProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
  val scalaspecs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.6" % "test"
  val scalchekc  = "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.8" % "test"
}
