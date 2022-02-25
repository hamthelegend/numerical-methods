package methods.open

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

data class SecantIterationResult(
    override val expression: Fx,
    override val iterations: List<SecantIteration>,
    override val terminationCause: TerminationCause,
) : IterationResult() {

    override val methodName = "Secant"

    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"

    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xA, xB, fxA, fxB, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        stringBuilder.append(terminationCause.message)
        return stringBuilder.toString()
    }

}

data class SecantIteration(
    val xA: RoundedDecimal,
    val xB: RoundedDecimal,
    val fxA: RoundedDecimal,
    val fxB: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage,
) : Iteration() {
    override fun toString() = "$xA, $xB, $fxA, $fxB, $xNew, $error"
}

fun runSecant(
    f: Fx,
    initialXA: BigDecimal,
    initialXB: BigDecimal,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
): SecantIterationResult {

    var iterator = 0
    val iterations = mutableListOf<SecantIteration>()

    var xOld: BigDecimal? = null
    var xA = initialXA
    var xB = initialXB

    var error = getMaxError(scale, roundingMode)

    while (true) {

        if (iterator >= minIterations && error.value.isZero) {
            return SecantIterationResult(f, iterations, TerminationCause.ZeroErrorReached)
        } else if (iterator >= maxIterations) {
            return SecantIterationResult(f, iterations, TerminationCause.MaxIterationsReached)
        }

        val fxA = f.calculate(xA, scale, roundingMode)
        val fxB = f.calculate(xB, scale, roundingMode)

        val xNew = xA - fxA * (xA - xB).divide(fxA - fxB, scale, roundingMode)

        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            scale = scale,
            roundingMode = roundingMode
        )

        iterations.add(
            SecantIteration(
                xA = xA.round(scale, roundingMode),
                xB = xB.round(scale, roundingMode),
                fxA = fxA.round(scale, roundingMode),
                fxB = fxB.round(scale, roundingMode),
                xNew = xNew.round(scale, roundingMode),
                error = error,
            )
        )

        xA = xB
        xB = xNew

        xOld = xNew
        iterator++

    }

}