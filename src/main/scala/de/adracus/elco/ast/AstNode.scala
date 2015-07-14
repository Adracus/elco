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
      val childLines = calculateChildLines(children)
      List(name) ++ childLines.lastMap({ lines =>
        lines.firstMap("├── " + _, "│   " + _)
      }, { lastLines =>
        lastLines.firstMap("└── " + _, "    " + _)
      }).flatten
    }
  }

  def calculateChildLines(items: List[Any]): List[List[String]] = {
    val flattened = flatten(items)
    flattened.collect {
      case a: AstNode =>
        a.toFormattedInner

      case default =>
        List(default.toString)
    }
  }

  private def flatten(list: List[Any]) = {
    def recurse(acc: List[Any], rest: List[Any]): List[Any] = rest match {
      case head :: tail => head match {
        case l: List[Any] => recurse(acc ++ l, tail)

        case default => recurse(acc :+ head, tail)
      }

      case Nil => acc
    }

    recurse(List.empty, list)
  }

  def toTreeString = toFormattedInner.mkString("\n")
}

object Empty extends AstNode
