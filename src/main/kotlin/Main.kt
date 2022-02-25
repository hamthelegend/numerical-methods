
import methods.common.Fx
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.open.runFixedPoint
import methods.open.runNewtonRaphson
import methods.open.runSecant
import java.math.BigDecimal

fun main() {
    val i = 100
    Fx("exp(-x) - x").apply {
        val xL = BigDecimal.ONE
        val xR = BigDecimal.ZERO
        runBisection(xL, xR, i).writeFile()
        runFalsePosition(xL, xR, i).writeFile()
        runNewtonRaphson(Fx("-exp(-x) - 1"), i).writeFile()
        runSecant(maxIterations = 100).writeFile()
    }
    Fx("exp(-x)").runFixedPoint(i).writeFile()
}