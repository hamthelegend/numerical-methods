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

    override val columnNamesCsv = Csv(
        "i",
        "xOld",
        "yOld",
        "yPrimeOld",
        "xNew",
        "error",
    )

    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"

}

data class NewtonRaphsonIteration(
    val xOld: RoundedDecimal,
    val yOld: RoundedDecimal,
    val yPrimeOld: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage,
) : Iteration() {

    override val valuesCsv = Csv(
        xOld,
        yOld,
        yPrimeOld,
        xNew,
        error,
    )

}

fun runNewtonRaphson(
    f: Fx,
    fPrime: Fx,
    initialX: BigDecimal,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
): NewtonRaphsonIterationResult {

    var iterator = 0

    val iterations = mutableListOf<NewtonRaphsonIteration>()

    var xOld = initialX
    var error = getMaxError(scale, roundingMode)

    while (true) {

        if (iterator >= minIterations && error.value.isZero) {
            return NewtonRaphsonIterationResult(f, iterations, TerminationCause.ZeroErrorReached)
        } else if(iterator >= maxIterations) {
            return NewtonRaphsonIterationResult(f, iterations, TerminationCause.MaxIterationsReached)
        }

        val fxOld = f.calculate(xOld, scale, roundingMode)
        val fPrimeXOld = fPrime.calculate(xOld, scale, roundingMode)

        val xNew = xOld - fxOld / fPrimeXOld
        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            scale = scale,
            roundingMode = roundingMode
        )

        iterations.add(
            NewtonRaphsonIteration(
                xOld = xOld.round(scale, roundingMode),
                yOld = fxOld.round(scale, roundingMode),
                yPrimeOld = fPrimeXOld.round(scale, roundingMode),
                xNew = xNew.round(scale, roundingMode),
                error = error,
            )
        )

        xOld = xNew

        iterator++

    }

}