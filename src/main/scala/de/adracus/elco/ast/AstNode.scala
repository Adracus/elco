package de.adracus.elco.ast

/**
 * Created by axel on 07/07/15.
 */
abstract class AstNode(input: Any*) {
  val children: List[Any] = input.toList

  lazy val name = this.getClass.getSimpleName

  def isLeaf = children.isEmpty

  def toFormattedInner: List[String] = {
    import util.RichIterable._
    if (isLeaf) List(name)
    else {
      val childLines = children.collect {
        case a: AstNode =>
          a.toFormattedInner

        case default =>
          List(default.toString)
      }
      List(name) ++ childLines.lastMap({ lines =>
        lines.firstMap("├── " + _, "│   " + _)
      }, { lastLines =>
        lastLines.firstMap("└── " + _, "    " + _)
      }).flatten
    }
  }

  def toTreeString = toFormattedInner.mkString("\n")
}

object Empty extends AstNode
