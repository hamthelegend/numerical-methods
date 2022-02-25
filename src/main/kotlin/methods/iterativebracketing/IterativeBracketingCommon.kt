package methods.iterativebracketing

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * An exception that is thrown when a bracket does not contain a 0 between the interval
 * The negative value must be on your xL and the positive value must be on your xR, or you will get this exception
 */
class NoZeroInBracketException(xL: BigDecimal, xR: BigDecimal, yL: BigDecimal, yR: BigDecimal) :
    Exception("There is no 0 between f($xL) = $yL and f($xR) = $yR")

/**
 * A data class that stores the result of all the iterations of an iterative bracketing numerical method
 *
 * A data class that extends [List] of [BracketIteration]
 */
data class BracketIterationResult(override val methodName: String, override val iterations: List<BracketIteration>) :
    IterationResult() {

    /**
     * Writes the result into a large CSV string
     * Every iteration follows the format "i, xL, xR, yL, yR, xNew, yNew"
     * Every iteration is separated by a line break
     * Its result can be tabulated by applications like Excel
     */
    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xL, xR, yL, yR, xNew, yNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        return stringBuilder.toString()
    }

}

/**
 * A data class that stores the result of a single iteration of an iterative bracketing numerical method
 */
data class BracketIteration(
    val xL: RoundedDecimal,
    val xR: RoundedDecimal,
    val yL: RoundedDecimal,
    val yR: RoundedDecimal,
    override val xNew: RoundedDecimal,
    val yNew: RoundedDecimal,
    override val error: Percentage,
    override val scale: Int = DEFAULT_CALCULATION_SCALE,
    override val roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
) : Iteration() {

    /**
     * Writes the iteration into a single-line CSV string
     * It follows the format "xL, xR, yL, yR, xNew, yNew, error"
     */
    override fun toString() = "$xL, $xR, $yL, $yR, $xNew, $yNew, $error"
}

/**
 * Uses an iterative bracketing method to approximate a solution of a function
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 * @param xNFormula is a lambda that accepts 4 parameters (xL, xR, yL, yR) to calculate the new x approximation
 *
 * @return is the list of all the iterations
 */
// TODO: Make xL and xR switchable
fun Fx.runIterativeBracketing(
    methodName: String,
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
    xNFormula: (xL: BigDecimal, xR: BigDecimal, yL: BigDecimal, yR: BigDecimal) -> BigDecimal,
): BracketIterationResult {

    val iterations = mutableListOf<BracketIteration>()

    var xL = initialXL
    var xR = initialXR

    var xOld: BigDecimal? = null

    for (i in 1..numberOfIterations) {

        val yL = calculate(xL, calculationScale, roundingMode)
        val yR = calculate(xR, calculationScale, roundingMode)

        if (BigDecimal.ZERO !in yL..yR) throw NoZeroInBracketException(xL, xR, yL, yR)

        val xNew = xNFormula(xL, xR, yL, yR)
        val yNew = calculate(xNew, calculationScale, roundingMode)

        val error: BigDecimal = xOld?.let {
            calculateError(xOld = it, xNew = xNew, scale = calculationScale, roundingMode = roundingMode)
        } ?: BigDecimal.ONE

        iterations.add(
            BracketIteration(
                xL = xL.round(outputScale, roundingMode),
                xR = xR.round(outputScale, roundingMode),
                yL = yL.round(outputScale, roundingMode),
                yR = yR.round(outputScale, roundingMode),
                xNew = xNew.round(outputScale, roundingMode),
                yNew = yNew.round(outputScale, roundingMode),
                error = error.toPercentage(outputScale, roundingMode),
                scale = outputScale,
                roundingMode = roundingMode,
            )
        )

        xOld = xNew

        if (BigDecimal.ZERO in yL..yNew) xR = xNew
        else xL = xNew

    }

    return BracketIterationResult(methodName, iterations)

}

/**
 * Is a function that tries to find a good bracket interval
 * WARNING: This function is not guaranteed to work
 * TODO: Make this more reliable
 */
@Suppress("unused")
fun Fx.findInterval(
    scale: Int = DEFAULT_CALCULATION_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): Pair<Int, Int> {
    val y0 = calculate(BigDecimal.ZERO, scale, roundingMode)

    if (y0 >= BigDecimal.ZERO) {
        var x = 0
        var y = calculate(BigDecimal.ZERO, scale, roundingMode)
        while (y >= BigDecimal.ZERO) {
            x--
            y = calculate(x.toBigDecimal(), scale, roundingMode)
        }
        return x to x + 1
    } else {
        var x = 0
        var y = calculate(BigDecimal.ZERO, scale, roundingMode)
        while (y < BigDecimal.ZERO) {
            x++
            y = calculate(x.toBigDecimal(), scale, roundingMode)
        }
        return x - 1 to x
    }

}