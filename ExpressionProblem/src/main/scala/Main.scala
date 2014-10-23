import expr.Expr._
import expr._

/**
 * Created by jooyung.han on 10/23/14.
 */
object Main {
  def main(args: Array[String]): Unit = {
    println(plus(
      Value(1),
      divide(
        multiply(
          Variable("x"),
          plus(
            Variable("y"),
            Value(0))
        ),
        Value(3)
      ),
      Variable("x")
    ).simplify.print())

    println(minus(Value(1), Value(2), Value(3)).print())
    println(minus(Value(1), minus(Value(2), Value(3))).print())
  }
}
