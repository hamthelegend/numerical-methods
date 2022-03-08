import methods.common.Default
import methods.common.Fx
import methods.common.writeFiles
import methods.iterativebracketing.BracketInterval
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.open.runFixedPoint
import methods.open.runNewtonRaphson
import methods.open.runSecant
import java.math.RoundingMode

fun main() {
    /* START OF EDITABLE VALUES */
    val f = Fx(expression = "x - cos(x)") // f(x) = f || f(x) = x - cos(x)
    val interval = BracketInterval(xL = 0, xR = 1) // Your bisection and false position interval
    val g = Fx("cos(x)") // x = g || x = g(x) in fixed point
    val fPrime = Fx("1 + sin(x)") // first derivative of f, used in Newton Raphson
    val initialXA = 0.toBigDecimal() // initial x guess for fixed point and Newton Raphson, initial xA in secant
    val initialXB = 1.toBigDecimal() // initial xB in secant

    // the minimum iterations you want, even if the absolute relative approximate error is already zero
    val minIterations = Default.MIN_ITERATION
    // Example: val minIterations = 10

    // the maximum iterations you want, even if the absolute relative approximate error is not yet zero
    val maxIterations = Default.MAX_ITERATION
    // Example: val maxIterations = 100

    // the number of decimal places you want after the decimal point
    val scale = Default.SCALE
    // Example: val scale = 4

    // the way you want to round the number 5. hover over [RoundingMode] to see a table of the different types
    val roundingMode = RoundingMode.HALF_EVEN
    // Example: val roundingMode = RoundingMode.HALF_UP /* to always round up 5 */
    /* END OF EDITABLE VALUES */

    listOf(
        // Edit the values below this line at your own discretion
        runBisection(
            f = f,
            initialInterval = interval,
            minIterations = minIterations,
            maxIterations = maxIterations,
            scale = scale,
            roundingMode = roundingMode,
        ),

        runFalsePosition(
            f = f, initialInterval = interval,
            minIterations = minIterations,
            maxIterations = maxIterations,
            scale = scale,
            roundingMode = roundingMode,
        ),

        runFixedPoint(
            g = g, initialX = initialXA,
            minIterations = minIterations,
            maxIterations = maxIterations,
            scale = scale,
            roundingMode = roundingMode,
        ),

        runNewtonRaphson(
            f = f,
            fPrime = fPrime,
            initialX = initialXA,
            minIterations = minIterations,
            maxIterations = maxIterations,
            scale = scale,
            roundingMode = roundingMode,
        ),

        runSecant(
            f = f,
            initialXA = initialXA,
            initialXB = initialXB,
            minIterations = minIterations,
            maxIterations = maxIterations,
            scale = scale,
            roundingMode = roundingMode,
        )
    ).writeFiles()

}