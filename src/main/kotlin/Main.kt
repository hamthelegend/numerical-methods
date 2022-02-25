
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
    val f = Fx(expression = "x - cos(x)") // f(x) = f || f(x) = x - cos(x)
    val interval = BracketInterval(xL = 0, xR = 1) // Your bisection and false position interval
    val g = Fx("cos(x)")
    val fPrime = Fx("1 + sin(x)")
    val minIterations = Default.MIN_ITERATION
    val maxIterations = Default.MAX_ITERATION
    val scale = Default.SCALE
    val roundingMode = RoundingMode.HALF_EVEN
//    val initialXA = f.
//    val initialXB

    runBisection(f = f, initialInterval = interval).writeFile()
    runFalsePosition(f = f, initialInterval = interval).writeFile()
    runFixedPoint(g = g).writeFile()
    runNewtonRaphson(f = f, fPrime = fPrime).writeFile()
    runSecant(f = f).writeFile()
}