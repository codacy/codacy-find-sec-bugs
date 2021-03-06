package codacy.findbugssec

import java.io.File
import java.nio.file.Path

import com.codacy.tools.scala.seed.utils.CommandRunner

import scala.util.{Failure, Success, Try}


sealed trait Builder {
  val command: List[String]
  val pathComponents: Seq[String]
  def supported(path: Path): Boolean
  def targetOfDirectory(path: File): Option[String]

  private def buildWithCommand(command: List[String], path: Path): Try[Boolean] = {
    CommandRunner.exec(command, dir = Option(path.toFile)) match {
      case Left(failure) => Failure(failure)
      case Right(output) if output.exitCode != 0 =>
        Failure(new Exception("Can't compile project."))

      case Right(output) => Success(true)
    }
  }

  def build(path: Path): Try[Boolean] = {
    buildWithCommand(command, path)
  }
}

object MavenBuilder extends Builder {
  val command = List("mvn", "compile")
  val pathComponents = Seq("src", "main", "java")

  def supported(path: Path): Boolean = {
    path.toFile.list.contains("pom.xml")
  }

  def targetOfDirectory(path: File): Option[String] = {
    Some(Seq(path.getAbsolutePath, "target", "classes").mkString(File.separator))
  }

}

object SBTBuilder extends Builder {
  val command = List("sbt", "compile")
  val pathComponents = Seq("src", "main", "scala")

  def supported(path: Path): Boolean = {
    path.toFile.list.contains("build.sbt")
  }

  def targetOfDirectory(path: File): Option[String] = {
    val target = new File(Seq(path.getAbsolutePath, "target").mkString(File.separator))
    target.exists match {
      case true =>
        val potentialScalaDir = target.list.filter { case filepath => filepath.startsWith("scala-")}
        // TODO: Cleaner way to do this?
        val scalaDirectory = potentialScalaDir.headOption.fold("") { case target => target}
        Some(Seq(target.getAbsolutePath, scalaDirectory, "classes").mkString(File.separator))
      case false =>
        Option.empty
    }
  }
}


object BuilderFactory {

  lazy val knownBuilders = Seq(
    MavenBuilder,
    SBTBuilder
  )

  def apply(path: Path): Option[Builder] = {
    val builders = knownBuilders.filter{ case builder => builder.supported(path)}
    builders.headOption
  }

}