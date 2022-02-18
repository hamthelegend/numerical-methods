package methods.iterativebracketing

import methods.DEFAULT_ROUNDING_MODE
import methods.DEFAULT_SCALE
import methods.calculate
import org.mariuszgromada.math.mxparser.Expression
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
 * @param scale is the scale that the BigDecimal will use
 * @param roundingMode is the RoundingMode that the BigDecimal will use
 * @param xNFormula is a lambda that accepts 4 parameters (xL, xR, yL, yR) to calculate the new x approximation
 *
 * @return is the list of all the iterations
 */
fun Expression.iterativeBracketing(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    scale: Int = DEFAULT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
    xNFormula: (BigDecimal, BigDecimal, BigDecimal, BigDecimal) -> BigDecimal,
): BracketIterationResult {

    val iterations = mutableListOf<BracketIteration>()

    var xL = initialXL.setScale(scale, roundingMode)
    var xR = initialXR.setScale(scale, roundingMode)

    for (i in 1..numberOfIterations) {

        val yL = calculate(xL, scale, roundingMode)
        val yR = calculate(xR, scale, roundingMode)

        if (BigDecimal.ZERO !in yL..yR) throw NoZeroInBracketException(xL, xR, yL, yR)

        val xN = xNFormula(xL, xR, yL, yR)
        val yN = calculate(xN, scale, roundingMode)

        iterations.add(BracketIteration(xL = xL, xR = xR, yL = yL, yR = yR, xN = xN, yN = yN))

        if (BigDecimal.ZERO in yL..yN) xR = xN
        else xL = xN

    }

    return BracketIterationResult(iterations)

}

/**
 * Is a function that tries to find a good bracket interval
 * WARNING: This function is not guaranteed to work
 */
fun Expression.findInterval(): Pair<Int, Int> {
    val y0 = calculate(BigDecimal.ZERO)

    if (y0 >= BigDecimal.ZERO) {
        var x = 0
        var y = calculate(BigDecimal.ZERO)
        while (y >= BigDecimal.ZERO) {
            x--
            y = calculate(x.toBigDecimal())
        }
        return x to x + 1
    } else {
        var x = 0
        var y = calculate(BigDecimal.ZERO)
        while (y < BigDecimal.ZERO) {
            x++
            y = calculate(x.toBigDecimal())
        }
        return x - 1 to x
    }

}

/**
 * Writes the results of all the iterations of an iterative bracketing numerical method to a CSV file
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 * @param methodName is the name of the iterative bracketing numerical method
 * @param method is a lambda (initialXL, initialXR, numberOfIterations) that calls an iterative bracketing numerical method
 */
fun Expression.writeFile(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    methodName: String,
    method: Expression.(BigDecimal, BigDecimal, Int) -> BracketIterationResult
) {
    val result = this.method(initialXL, initialXR, numberOfIterations)
    println("$methodName: x ≈ ${result.iterations.last().xN}")
    val fileName = "$methodName.csv"
    val file = File(fileName)
    file.writeText(result.toString())
    println("Answer written to $fileName")
}