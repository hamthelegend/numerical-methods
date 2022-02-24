package methods

import com.mpobjects.bdparsii.eval.Parser
import com.mpobjects.bdparsii.eval.Scope
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

const val DEFAULT_CALCULATION_SCALE = 64
const val DEFAULT_OUTPUT_SCALE = 4
val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP

abstract class IterationResult {
    abstract val methodName: String
    abstract val iterations: List<Iteration>

    fun writeFile() {
        println("$methodName: x ≈ ${iterations.last().xNew}")
        val fileName = "$methodName.csv"
        val file = File(fileName)
        file.writeText(toString())
        println("Answer written to $fileName")
    }

}

abstract class Iteration {
    abstract val xOld: BigDecimal?
    abstract val xNew: BigDecimal
    abstract val scale: Int
    abstract val roundingMode: RoundingMode
    val error: BigDecimal?
        get() =  xOld?.minus(xNew)?.divide(xNew, scale, roundingMode)?.abs() // ((xNew - xOld?) / xNew).abs()
}

data class Fx(val expression: String)

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
fun Fx.calculate(
    x: BigDecimal,
    scale: Int = DEFAULT_CALCULATION_SCALE,
    roundingMode: RoundingMode,
): BigDecimal {
    val scope = Scope()
    val xVariable = scope.getVariable("x")
    val expression = Parser.parse(expression, scope)
    xVariable.value = x
    return expression.evaluate().setScale(scale, roundingMode)
}