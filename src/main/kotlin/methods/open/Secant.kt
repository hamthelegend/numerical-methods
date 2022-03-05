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

    override val columnNamesCsv = Csv(
        "i",
        "xA",
        "xB",
        "yA",
        "yB",
        "xNew",
        "error",
    )

    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"


}

data class SecantIteration(
    val xA: RoundedDecimal,
    val xB: RoundedDecimal,
    val yA: RoundedDecimal,
    val yB: RoundedDecimal,
    override val xNew: RoundedDecimal,
    override val error: Percentage?,
) : Iteration() {
    override val valuesCsv = Csv(
        xA,
        xB,
        yA,
        yB,
        xNew,
        error,
    )
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

    var error: Percentage? = null

    while (true) {

        if (iterator >= minIterations && error?.value?.isZero == true) {
            return SecantIterationResult(f, iterations, TerminationCause.ZeroErrorReached)
        } else if (iterator >= maxIterations) {
            return SecantIterationResult(f, iterations, TerminationCause.MaxIterationsReached)
        }

        val yA = f.calculate(xA, scale, roundingMode)
        val yB = f.calculate(xB, scale, roundingMode)

        val xNew = xA - yA * (xA - xB).divide(yA - yB, scale, roundingMode)

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
                yA = yA.round(scale, roundingMode),
                yB = yB.round(scale, roundingMode),
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