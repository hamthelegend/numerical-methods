package methods

import com.mpobjects.bdparsii.eval.Parser
import com.mpobjects.bdparsii.eval.Scope
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToInt

val DEFAULT_SCALE = 64
val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN

/**
 * Calculates the value of an expression that is a function of x, given x
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param x is the value of x that you will substitute to the expression
 * @param scale is the scale that the BigDecimal will use
 * @param roundingMode is the RoundingMode that the BigDecimal will use
 *
 * @return the answer to f(x) given x
 */
fun Expression.calculate(
    x: BigDecimal,
    scale: Int = DEFAULT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE
): BigDecimal {
    val scope = Scope()
    val xVariable = scope.getVariable("x")
    val expression = Parser.parse(expressionString, scope)
    xVariable.value = x
    return expression.evaluate().setScale(scale, roundingMode)
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