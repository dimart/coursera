package calculator

sealed abstract class Expr
final case class Literal(v: Double) extends Expr
final case class Ref(name: String) extends Expr
final case class Plus(a: Expr, b: Expr) extends Expr
final case class Minus(a: Expr, b: Expr) extends Expr
final case class Times(a: Expr, b: Expr) extends Expr
final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(
      namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
    namedExpressions.map(t => (t._1, Signal(eval(t._2(), namedExpressions))))
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    def eval_(expr: Expr, nTimes: Int): Double = {
      if (nTimes < 0) Double.NaN else
      expr match {
        case Literal(x) => x
        case Ref(x) => eval_( references.getOrElse(x, Signal(Literal(Double.NaN)))(), nTimes - 1 )
        case Plus(x, y) => eval_(x, nTimes - 1) + eval_(y, nTimes - 1)
        case Minus(x, y) => eval_(x, nTimes - 1) - eval_(y, nTimes - 1)
        case Times(x, y) => eval_(x, nTimes - 1) * eval_(y, nTimes - 1)
        case Divide(x, y) => eval_(x, nTimes - 1) / eval_(y, nTimes - 1)
      }
    }
    eval_(expr, 11)
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
