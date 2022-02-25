
import methods.common.Fx
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.open.runFixedPoint
import methods.open.runNewtonRaphson
import methods.open.runSecant
import java.math.BigDecimal

fun main() {
    val minIterations = 5
    val maxIterations = 100
    Fx("exp(-x) - x").apply {
        val xL = BigDecimal.ONE
        val xR = BigDecimal.ZERO
        runBisection(xL, xR, minIterations, maxIterations).writeFile()
        runFalsePosition(xL, xR, minIterations, maxIterations).writeFile()
        runNewtonRaphson(Fx("-exp(-x) - 1"), minIterations, maxIterations).writeFile()
        runSecant(minIterations = minIterations, maxIterations = 100).writeFile()
    }
    Fx("exp(-x)").runFixedPoint(minIterations = minIterations, maxIterations = maxIterations).writeFile()
}