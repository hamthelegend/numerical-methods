package methods

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Calculates the value of an expression that is a function of x, given x
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param x is the value of x that you will substitute to the expression
 * @param functionName is the name of the function; currently unused
 *
 * @return the answer to f(x) given x
 */
fun Expression.calculate(x: Double, functionName: String = "f"): Double {
    val function = Function("$functionName(x) = $expressionString")
    val functionCallExpression = Expression("$functionName($x)", function)
    return functionCallExpression.calculate()
}

/**
 * A function to round a double to a number of decimal places
 *
 * @receiver is the value that you want to round
 *
 * @param decimalPlaces is the number of digits after the decimal point that you want to preserve
 *
 * @return the rounded value
 */
@Suppress("unused")
fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(decimalPlaces.toDouble())
    return (this * factor).roundToInt() / factor
}