import methods.iterativebracketing.findInterval
import methods.iterativebracketing.writeBisection
import methods.iterativebracketing.writeFalsePosition
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal

fun main() {
    iterativeBracketing(
        expression = Expression("x * e^x - cos(x)"),
//        initialXL = 0.0,
//        initialXR = 1.0,
        numberOfIterations = 100,
        runBisection = true,
        runFalsePosition = true,
    )

//    interactiveIterativeBracketing()
}

@Suppress("unused")
fun iterativeBracketing(
    expression: Expression,
    numberOfIterations: Int,
    runBisection: Boolean,
    runFalsePosition: Boolean,
) {
    val interval = expression.findInterval()
    iterativeBracketing(
        expression = expression,
        initialXL = interval.first.toBigDecimal(),
        initialXR = interval.second.toBigDecimal(),
        numberOfIterations = numberOfIterations,
        runBisection = runBisection,
        runFalsePosition = runFalsePosition,
    )
}

fun iterativeBracketing(
    expression: Expression,
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    runBisection: Boolean,
    runFalsePosition: Boolean,
) {

    if (runBisection) {
        println()
        expression.writeBisection(initialXL, initialXR, numberOfIterations)
    }

    if (runFalsePosition) {
        println()
        expression.writeFalsePosition(initialXL, initialXR, numberOfIterations)
    }

}

@Suppress("unused")
fun interactiveIterativeBracketing() {

    print("f(x) = ")
    val expression = Expression(readln())

    print("Do you want to automatically get xR and xL? It is not guaranteed to work. (y/n) ")
    val useBracketFinder = readln()
    val interval = if (useBracketFinder == "y") expression.findInterval() else null
    if (interval != null) {
        println("xL = ${interval.first}")
        println("xR = ${interval.second}")
    }

    val xL = interval?.first?.toBigDecimal() ?: run {
        print("xL = ")
        readln().toBigDecimal()
    }

    val xR = interval?.second?.toBigDecimal() ?: run {
        print("xR = ")
        readln().toBigDecimal()
    }

    print("i = ")
    val i = readln().toInt()

    print("Run Bisection Method? (y/n) ")
    val runBisection = readln()

    print("Run False Position Method? (y/n) ")
    val runFalsePosition = readln()

    if (runBisection == "y") {
        println()
        expression.writeBisection(xL, xR, i)
    }

    if (runFalsePosition == "y") {
        println()
        expression.writeFalsePosition(xL, xR, i)
    }

}