package de.adracus.elco.production.expressions

import de.adracus.elco.ast.AstNode

/**
 * Created by axel on 19/08/15.
 */
case class ElcoFile(expression: Expression) extends AstNode(expression)
