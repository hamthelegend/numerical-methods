import methods.DEFAULT_OUTPUT_SCALE
import methods.DEFAULT_ROUNDING_MODE
import methods.DEFAULT_SCALE
import methods.iterativebracketing.findInterval
import methods.iterativebracketing.writeBisection
import methods.iterativebracketing.writeFalsePosition
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    iterativeBracketing(
        expression = Expression("x * exp(x) - cos(x)"),
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
    scale: Int = DEFAULT_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
    runBisection: Boolean,
    runFalsePosition: Boolean,
) {
    val interval = expression.findInterval(scale, roundingMode)
    iterativeBracketing(
        expression = expression,
        initialXL = interval.first.toBigDecimal(),
        initialXR = interval.second.toBigDecimal(),
        numberOfIterations = numberOfIterations,
        scale = scale,
        outputScale = outputScale,
        runBisection = runBisection,
        runFalsePosition = runFalsePosition,
    )
}

fun iterativeBracketing(
    expression: Expression,
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    scale: Int = DEFAULT_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
    runBisection: Boolean,
    runFalsePosition: Boolean,
) {

    if (runBisection) {
        println()
        expression.writeBisection(
            initialXL = initialXL,
            initialXR = initialXR,
            numberOfIterations = numberOfIterations,
            scale = scale,
            outputScale = outputScale,
            roundingMode = roundingMode
        )
    }

    if (runFalsePosition) {
        println()
        expression.writeFalsePosition(
            initialXL = initialXL,
            initialXR = initialXR,
            numberOfIterations = numberOfIterations,
            scale = scale,
            outputScale = outputScale,
            roundingMode = roundingMode
        )
    }

}

@Suppress("unused")
fun interactiveIterativeBracketing() {

    print("scale = ")
    val scale = readln().toInt()

    print("f(x) = ")
    val expression = Expression(readln())

    print("Do you want to automatically get xR and xL? It is not guaranteed to work. (y/n) ")
    val useBracketFinder = readln()
    val interval = if (useBracketFinder == "y") expression.findInterval(scale) else null
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
        expression.writeBisection(
            initialXL = xL,
            initialXR = xR,
            numberOfIterations = i,
            scale = scale * 2,
            outputScale = scale
        )
    }

    if (runFalsePosition == "y") {
        println()
        expression.writeFalsePosition(
            initialXL = xL,
            initialXR = xR,
            numberOfIterations = i,
            scale = scale * 2,
            outputScale = scale
        )
    }

}