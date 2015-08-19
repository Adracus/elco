package de.adracus.elco.production

import de.adracus.elco.ast.AstNode
import de.adracus.elco.production.expressions.ElcoFile

/**
 * Created by axel on 18/08/15.
 */
object CodeGenerator {
  def generate(astNode: AstNode): String = astNode match {
    case default => throw new Exception(s"Illegal $default")
  }
}
