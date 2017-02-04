package com.originate.support

import play.api.db.evolutions.{ClassLoaderEvolutionsReader, Evolutions, ResourceEvolutionsReader}

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.io.Source
import scala.util.Try

/**
 * EvolutionTransformingReader hooks into the evolution running process during testing
 * and gives you the ability to transform SQL statements before they are run. This is often
 * useful for things that don't translate 100% between your DB's SQL dialect and H2's
 * dialect.
 *
 * Example: renaming columns in H2 has a different syntax than in PostGres
 */
class EvolutionTransformingReader(
    classLoader: ClassLoader = classOf[ClassLoaderEvolutionsReader].getClassLoader,
    prefix: String = "")
  extends ResourceEvolutionsReader {

  def loadResource(db: String, revision: Int): Option[InputStream] =
    for {
      stream <- Option(classLoader.getResourceAsStream(prefix + Evolutions.resourceName(db, revision)))
      lines <- Try(Source.fromInputStream(stream).getLines).toOption
      updated = lines map convertPostgresLinesToH2
    } yield convertLinesToInputStream(updated)

  private val ColumnRename = """(?i)\s*ALTER TABLE (\w+) RENAME COLUMN (\w+) TO (\w+);""".r

  private def convertPostgresLinesToH2(line: String): String =
    line match {
      case ColumnRename(tableName, oldColumn, newColumn) =>
        s"""ALTER TABLE $tableName ALTER COLUMN $oldColumn RENAME TO $newColumn;"""
      case _ => line
    }

  private def convertLinesToInputStream(lines: Iterator[String]): InputStream =
    new ByteArrayInputStream(lines.mkString("\n").getBytes(StandardCharsets.UTF_8))
}
