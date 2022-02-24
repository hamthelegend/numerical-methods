package methods.iterativebracketing

import methods.*
import java.io.File
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
data class BracketIterationResult(val iterations: List<BracketIteration>) {

    /**
     * Writes the result into a large CSV string
     * Every iteration follows the format "i, xL, xR, yL, yR, xN, yN"
     * Every iteration is separated by a line break
     * Its result can be tabulated by applications like Excel
     */
    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xL, xR, yL, yR, xN, yN\n")
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
    val xL: BigDecimal,
    val xR: BigDecimal,
    val yL: BigDecimal,
    val yR: BigDecimal,
    val xN: BigDecimal,
    val yN: BigDecimal,
) {
    constructor(
        xL: BigDecimal,
        xR: BigDecimal,
        yL: BigDecimal,
        yR: BigDecimal,
        xN: BigDecimal,
        yN: BigDecimal,
        scale: Int,
        roundingMode: RoundingMode,
    ) : this(
        xL = xL.setScale(scale, roundingMode),
        xR = xR.setScale(scale, roundingMode),
        yL = yL.setScale(scale, roundingMode),
        yR = yR.setScale(scale, roundingMode),
        xN = xN.setScale(scale, roundingMode),
        yN = yN.setScale(scale, roundingMode),
    )

    /**
     * Writes the iteration into a single-line CSV string
     * It follows the format "xL, xR, yL, yR, xN, yN"
     */
    override fun toString() = "$xL, $xR, $yL, $yR, $xN, $yN"
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
fun Fx.runIterativeBracketing(
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

    for (i in 1..numberOfIterations) {

        val yL = calculate(xL, calculationScale, roundingMode)
        val yR = calculate(xR, calculationScale, roundingMode)

        if (BigDecimal.ZERO !in yL..yR) throw NoZeroInBracketException(xL, xR, yL, yR)

        val xN = xNFormula(xL, xR, yL, yR)
        val yN = calculate(xN, calculationScale, roundingMode)

        iterations.add(
            BracketIteration(
                xL = xL,
                xR = xR,
                yL = yL,
                yR = yR,
                xN = xN,
                yN = yN,
                scale = outputScale,
                roundingMode = roundingMode
            )
        )

        if (BigDecimal.ZERO in yL..yN) xR = xN
        else xL = xN

    }

    return BracketIterationResult(iterations)

}

/**
 * Is a function that tries to find a good bracket interval
 * WARNING: This function is not guaranteed to work
 */
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

/**
 * Writes the results of all the iterations of an iterative bracketing numerical method to a CSV file
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param methodName is the name of the iterative bracketing numerical method
 */
fun BracketIterationResult.writeFile(
    methodName: String,
) {
    println("$methodName: x ≈ ${iterations.last().xN}")
    val fileName = "$methodName.csv"
    val file = File(fileName)
    file.writeText(toString())
    println("Answer written to $fileName")
}