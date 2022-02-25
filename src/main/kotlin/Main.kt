
import methods.common.Fx
import methods.iterativebracketing.BracketInterval
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.open.runFixedPoint
import methods.open.runNewtonRaphson
import methods.open.runSecant

fun main() {
    val f = Fx("x - cos(x)")
    val interval = BracketInterval(0, 1)
    runBisection(f = f, initialInterval = interval).writeFile()
    runFalsePosition(f = f, initialInterval = interval).writeFile()
    runFixedPoint(g = Fx("cos(x)")).writeFile()
    runNewtonRaphson(f = f, fPrime = Fx("1 + sin(x)")).writeFile()
    runSecant(f = f).writeFile()
}