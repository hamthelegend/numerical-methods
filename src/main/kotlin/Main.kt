
import methods.common.Default
import methods.common.Fx
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

    // the maximum iterations you want, even if the absolute relative approximate error is not yet zero
    val maxIterations = Default.MAX_ITERATION

    // the number of decimal places you want after the decimal point
    val scale = Default.SCALE

    // the way you want to round the number 5. hover over [RoundingMode] to see a table of the different types
    val roundingMode = RoundingMode.HALF_EVEN
    /* END OF EDITABLE VALUES */

    // Edit the values below this line at your own discretion
    runBisection(
        f = f,
        initialInterval = interval,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ).writeFile()

    runFalsePosition(
        f = f, initialInterval = interval,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ).writeFile()

    runFixedPoint(
        g = g, initialX = initialXA,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ).writeFile()

    runNewtonRaphson(
        f = f,
        fPrime = fPrime,
        initialX = initialXA,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ).writeFile()

    runSecant(
        f = f,
        initialXA = initialXA,
        initialXB = initialXB,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ).writeFile()

}