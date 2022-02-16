package methods.iterativebracketing

import methods.calculate
import org.mariuszgromada.math.mxparser.Expression
import java.io.File

/**
 * An exception that is thrown when a bracket does not contain a 0 between the interval
 * The negative value must be on your xL and the positive value must be on your xR, or you will get this exception
 */
class NoZeroInBracketException(xL: Double, xR: Double, yL: Double, yR: Double) :
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
    val xL: Double,
    val xR: Double,
    val yL: Double,
    val yR: Double,
    val xN: Double,
    val yN: Double,
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
 * @param xNFormula is a lambda that accepts 4 parameters (xL, xR, yL, yR) to calculate the new x approximation
 *
 * @return is the list of all the iterations
 */
fun Expression.iterativeBracketing(
    initialXL: Double,
    initialXR: Double,
    numberOfIterations: Int,
    xNFormula: (Double, Double, Double, Double) -> Double,
): BracketIterationResult {

    val iterations = mutableListOf<BracketIteration>()

    var xL = initialXL
    var xR = initialXR

    for (i in 1..numberOfIterations) {

        val yL = calculate(xL)
        val yR = calculate(xR)

        if (0.0 !in yL..yR) throw NoZeroInBracketException(xL, xR, yL, yR)

        val xN = xNFormula(xL, xR, yL, yR)
        val yN = calculate(xN)

        iterations.add(BracketIteration(xL = xL, xR = xR, yL = yL, yR = yR, xN = xN, yN = yN))

        if (0.0 in yL..yN) xR = xN
        else xL = xN

    }

    return BracketIterationResult(iterations)

}

/**
 * Is a function that tries to find a good bracket interval
 * WARNING: This function is not guaranteed to work
 */
fun Expression.findInterval(): Pair<Int, Int> {
    val y0 = calculate(0.0)

    if (y0 >= 0) {
        var x = 0
        var y = calculate(0.0)
        while (y >= 0) {
            x--
            y = calculate(x.toDouble())
        }
        return x to x + 1
    } else {
        var x = 0
        var y = calculate(0.0)
        while (y < 0) {
            x++
            y = calculate(x.toDouble())
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
    initialXL: Double,
    initialXR: Double,
    numberOfIterations: Int,
    methodName: String,
    method: Expression.(Double, Double, Int) -> BracketIterationResult
) {
    val result = this.method(initialXL, initialXR, numberOfIterations)
    println("$methodName: x ≈ ${result.iterations.last().xN}")
    val fileName = "$methodName.csv"
    val file = File(fileName)
    file.writeText(result.toString())
    println("Answer written to $fileName")
}