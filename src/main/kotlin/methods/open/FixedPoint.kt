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

fun runFixedPoint(
    g: Fx,
    initialX: BigDecimal,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
): FixedPointIterationResult {

    var iterator = 0

    val iterations = mutableListOf<FixedPointIteration>()
    var xOld = initialX
    var error = getMaxError(scale, roundingMode)

    while(true) {

        if (iterator >= minIterations && error.value.isZero) {
            return FixedPointIterationResult(g, iterations, TerminationCause.ZeroErrorReached)
        } else if(iterator >= maxIterations) {
            return FixedPointIterationResult(g, iterations, TerminationCause.MaxIterationsReached)
        }

        val xNew = g.calculate(xOld, scale, roundingMode)

        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            scale = scale,
            roundingMode = roundingMode
        )

        iterations.add(
            FixedPointIteration(
                xOld = xOld.round(scale, roundingMode),
                xNew = xNew.round(scale, roundingMode),
                error = error,
            )
        )

        xOld = xNew

        iterator++

    }

}