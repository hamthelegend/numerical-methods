
import methods.Fx
import methods.iterativebracketing.findInterval
import methods.iterativebracketing.runBisection
import methods.iterativebracketing.runFalsePosition
import methods.openmethod.runFixedPoint

fun main() {
    val i = 100
    Fx("x * exp(x) - cos(x)").apply {
        val (xL, xR) = findInterval().toList().map { it.toBigDecimal() }
        runBisection(xL, xR, i).writeFile()
        runFalsePosition(xL, xR, i).writeFile()
    }
    Fx("cos(x) / exp(x)").runFixedPoint(i).writeFile()
}