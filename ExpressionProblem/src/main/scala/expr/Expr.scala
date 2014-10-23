package expr
import scala.language.implicitConversions

/**
 * Created by jooyung.han on 10/23/14.
 */

object Expr {
  type EvaluationContext = Map[String, Double]

  class Operator(op: Char) {
    def precedence: Int = if (op == '+' || op == '-') 1 else 2

    def evaluate(l: Double, r: Double): Double = op match {
      case '+' => l + r
      case '-' => l - r
      case '*' => l * r
      case '/' => l / r
    }
  }

  implicit def charToOperator(op: Char): Operator = new Operator(op)

  def plus(e: Expr*): Expr = bin('+', e: _*)

  def minus(e: Expr*): Expr = bin('-', e: _*)

  def multiply(e: Expr*): Expr = bin('*', e: _*)

  def divide(e: Expr*): Expr = bin('/', e: _*)

  private def bin(op: Char, e: Expr*): Expr = {
    require(!e.isEmpty)
    e.reduce(BinaryOperator(op, _, _))
  }
}

sealed trait Expr {

  import expr.Expr._

  def evaluate(implicit context: EvaluationContext): Double = this match {
    case BinaryOperator(op, left, right) => op.evaluate(left.evaluate, right.evaluate)
    case Value(value) => value
    case Variable(name) => context(name)
  }

  def simplify(): Expr = this match {
    case BinaryOperator(op, left, right) => {
      BinaryOperator(op, left.simplify, right.simplify) match {
        case BinaryOperator('+', Value(0.0), right) => right
        case BinaryOperator('+', left, Value(0.0)) => left
        case BinaryOperator('*', Value(1.0), right) => right
        case BinaryOperator('*', Value(0.0), right) => Value(0.0)
        case e => e
      }
    }
    case e => e
  }

  def print(precedence: Int = 0): String = this match {
    case Variable(name) => name
    case Value(value) => value.toString
    case BinaryOperator(op, left, right) => {
      val (l, r) = (left.print(op.precedence), right.print(op.precedence))
      if (precedence >= op.precedence)
        "(" + l + op + r + ")"
      else
        l + op + r
    }
  }
}

case class BinaryOperator(op: Char, left: Expr, right: Expr) extends Expr

case class Value(value: Double) extends Expr

case class Variable(name: String) extends Expr

