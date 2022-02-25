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

fun Fx.runSecant(
    initialXA: BigDecimal = guessInitialX(),
    initialXB: BigDecimal =
        guessInitialX(if (initialXA >= BigDecimal.ZERO) initialXA + BigDecimal.ONE else initialXA - BigDecimal.ONE),
    minIterations: Int,
    maxIterations: Int,
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): SecantIterationResult {

    var iterator = 0
    val iterations = mutableListOf<SecantIteration>()

    var xOld: BigDecimal? = null
    var xA = initialXA
    var xB = initialXB

    var error = getMaxError(outputScale, roundingMode)

    while (true) {

        if (iterator >= minIterations && error.value.isZero) {
            return SecantIterationResult(this, iterations, TerminationCause.ZeroErrorReached)
        } else if (iterator >= maxIterations) {
            return SecantIterationResult(this, iterations, TerminationCause.MaxIterationsReached)
        }

        val fxA = calculate(xA, calculationScale, roundingMode)
        val fxB = calculate(xB, calculationScale, roundingMode)

        val xNew = xA - fxA * (xA - xB).divide(fxA - fxB, calculationScale, roundingMode)

        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            calculationScale = calculationScale,
            outputScale = outputScale,
            roundingMode = roundingMode
        )

        iterations.add(
            SecantIteration(
                xA = xA.round(outputScale, roundingMode),
                xB = xB.round(outputScale, roundingMode),
                fxA = fxA.round(outputScale, roundingMode),
                fxB = fxB.round(outputScale, roundingMode),
                xNew = xNew.round(outputScale, roundingMode),
                error = error,
            )
        )

        xA = xB
        xB = xNew

        xOld = xNew
        iterator++

    }

}