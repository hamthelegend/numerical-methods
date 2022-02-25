package methods.open

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

data class NewtonRaphsonIterationResult(
    override val expression: Fx,
    override val iterations: List<NewtonRaphsonIteration>,
    override val terminationCause: TerminationCause,
) : IterationResult() {

    override val methodName = "Newton Raphson"

    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"

    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xOld, fXOld, fPrimeXOld, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        stringBuilder.append(terminationCause.message)
        return stringBuilder.toString()
    }
}

data class NewtonRaphsonIteration(
    val xOld: RoundedDecimal,
    val fXOld: RoundedDecimal,
    val fPrimeXOld: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage,
) : Iteration() {
    override fun toString() = "$xOld, $fXOld, $fPrimeXOld, $xNew, $error"
}

fun Fx.runNewtonRaphson(
    derivative: Fx,
    minIterations: Int,
    maxIterations: Int,
    initialX: BigDecimal = guessInitialX(),
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): NewtonRaphsonIterationResult {

    var iterator = 0

    val iterations = mutableListOf<NewtonRaphsonIteration>()

    var xOld = initialX
    var error = getMaxError(outputScale, roundingMode)

    while (true) {

        if (iterator >= minIterations && error.value.isZero) {
            return NewtonRaphsonIterationResult(this, iterations, TerminationCause.ZeroErrorReached)
        } else if(iterator >= maxIterations) {
            return NewtonRaphsonIterationResult(this, iterations, TerminationCause.MaxIterationsReached)
        }

        val fXOld = calculate(xOld, calculationScale, roundingMode)
        val fPrimeXOld = derivative.calculate(xOld, calculationScale, roundingMode)

        val xNew = xOld - fXOld / fPrimeXOld
        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            calculationScale = calculationScale,
            outputScale = outputScale,
            roundingMode = roundingMode
        )

        iterations.add(
            NewtonRaphsonIteration(
                xOld = xOld.round(outputScale, roundingMode),
                fXOld = fXOld.round(outputScale, roundingMode),
                fPrimeXOld = fPrimeXOld.round(outputScale, roundingMode),
                xNew = xNew.round(outputScale, roundingMode),
                error = error,
            )
        )

        xOld = xNew

        iterator++

    }

}