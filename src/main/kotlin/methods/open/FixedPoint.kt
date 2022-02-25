package methods.open

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

data class FixedPointIterationResult(
    override val iterations: List<FixedPointIteration>,
) : IterationResult() {
    override val methodName = "Fixed Point"
    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xOld, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        return stringBuilder.toString()
    }
}

data class FixedPointIteration(
    val xOld: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage,
    override val scale: Int,
    override val roundingMode: RoundingMode,
) : Iteration() {
    override fun toString() = "$xOld, $xNew, $error"
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
        val error = calculateError(xOld = xOld, xNew = xNew, scale = calculationScale, roundingMode = roundingMode)
        iterations.add(
            FixedPointIteration(
                xOld = xOld.round(outputScale, roundingMode),
                xNew = xNew.round(outputScale, roundingMode),
                error = error.toPercentage(outputScale, roundingMode),
                scale = outputScale,
                roundingMode = roundingMode,
            )
        )
        xOld = xNew
    }
    return FixedPointIterationResult(iterations)

}