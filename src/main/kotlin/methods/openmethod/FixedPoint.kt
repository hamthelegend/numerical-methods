package methods.openmethod

import methods.*
import java.math.BigDecimal
import java.math.RoundingMode

data class FixedPointIterationResult(
    override val iterations: List<FixedPointIteration>,
) : IterationResult() {
    override val methodName = "Fixed Point"
    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xOld, xNew\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        return stringBuilder.toString()
    }
}

class FixedPointIteration(
    xOld: BigDecimal,
    xNew: BigDecimal,
    override val scale: Int,
    override val roundingMode: RoundingMode,
) : Iteration() {
    override val xOld = xOld.setScale(scale, roundingMode)
    override val xNew = xNew.setScale(scale, roundingMode)

    override fun toString() = "$xOld, $xNew"
}

fun Fx.runFixedPoint(
    numberOfIterations: Int,
    initialX: BigDecimal = guessInitialX(),
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): FixedPointIterationResult {

    val iterations = mutableListOf<FixedPointIteration>()

    var xOld = initialX

    repeat(numberOfIterations) {

        val xNew = calculate(xOld, calculationScale, roundingMode)

        iterations.add(
            FixedPointIteration(
                xOld = xOld,
                xNew = xNew,
                scale = outputScale,
                roundingMode = roundingMode
            )
        )

        xOld = xNew

    }

    return FixedPointIterationResult(iterations)

}

fun Fx.guessInitialX(
    guessX: BigDecimal = BigDecimal.ZERO,
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): BigDecimal = try {
    calculate(guessX, calculationScale, roundingMode)
    guessX
} catch (e: ArithmeticException) {
    val newGuessX = if (guessX > BigDecimal.ZERO) -guessX else guessX + BigDecimal.ONE
    guessInitialX(newGuessX, calculationScale, roundingMode)
}