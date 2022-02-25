package methods.open

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

data class SecantIterationResult(
    override val expression: Fx,
    override val iterations: List<SecantIteration>,
) : IterationResult() {

    override val methodName = "Secant"

    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"

    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xA, xB, fxA, fxB, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
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

    var error = 1.toBigDecimal().toPercentage(outputScale, roundingMode)

    while(!error.value.isZero && iterator < maxIterations) {
        val fxA = calculate(xA, calculationScale, roundingMode)
        val fxB = calculate(xB, calculationScale, roundingMode)

        val xNew = xA - fxA * (xA - xB).divide(fxA - fxB, calculationScale, roundingMode)

        error = xOld?.let {
            calculateError(xOld = it, xNew = xNew, scale = calculationScale, roundingMode = roundingMode).toPercentage(
                outputScale, roundingMode
            )
        } ?: 1.toBigDecimal().toPercentage(outputScale, roundingMode)

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

    return SecantIterationResult(this, iterations)

}