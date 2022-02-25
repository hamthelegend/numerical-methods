
import methods.common.Fx
import methods.iterativebracketing.BracketInterval
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.open.runFixedPoint
import methods.open.runNewtonRaphson
import methods.open.runSecant

fun main() {
    val f = Fx("exp(-x) - x")
    val interval = BracketInterval(0, 1)
    runBisection(f, interval).writeFile()
    runFalsePosition(f, interval).writeFile()
    runNewtonRaphson(f, Fx("-exp(-x) - 1")).writeFile()
    runSecant(f).writeFile()
    runFixedPoint(g = Fx("exp(-x)")).writeFile()
}