package methods.open

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

data class FixedPointIterationResult(
    override val expression: Fx,
    override val iterations: List<FixedPointIteration>,
    override val terminationCause: TerminationCause,
) : IterationResult() {

    override val methodName = "Fixed Point"

    override fun getEquationString(xLast: RoundedDecimal) = "x = $expression"

    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xOld, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        stringBuilder.append(terminationCause.message)
        return stringBuilder.toString()
    }
}

data class FixedPointIteration(
    val xOld: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage,
) : Iteration() {
    override fun toString() = "$xOld, $xNew, $error"
}

fun Fx.runFixedPoint(
    minIterations: Int,
    maxIterations: Int,
    initialX: BigDecimal = guessInitialX(),
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): FixedPointIterationResult {

    var iterator = 0

    val iterations = mutableListOf<FixedPointIteration>()
    var xOld = initialX
    var error = getMaxError(outputScale, roundingMode)

    while(true) {

        if (iterator >= minIterations && error.value.isZero) {
            return FixedPointIterationResult(this, iterations, TerminationCause.ZeroErrorReached)
        } else if(iterator >= maxIterations) {
            return FixedPointIterationResult(this, iterations, TerminationCause.MaxIterationsReached)
        }

        val xNew = calculate(xOld, calculationScale, roundingMode)

        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            calculationScale = calculationScale,
            outputScale = outputScale,
            roundingMode = roundingMode
        )

        iterations.add(
            FixedPointIteration(
                xOld = xOld.round(outputScale, roundingMode),
                xNew = xNew.round(outputScale, roundingMode),
                error = error,
            )
        )

        xOld = xNew

        iterator++

    }

}