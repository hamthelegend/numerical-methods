package methods.iterativebracketing

import methods.common.*
import java.math.BigDecimal
import java.math.RoundingMode

class NoZeroInIntervalException(xL: BigDecimal, xR: BigDecimal, yL: BigDecimal, yR: BigDecimal) :
    Exception("There is no 0 between f($xL) = $yL and f($xR) = $yR")

data class BracketInterval(val xL: BigDecimal, val xR: BigDecimal) {
    constructor(xL: Int, xR: Int) : this(xL.toBigDecimal(), xR.toBigDecimal())
}

data class BracketIterationResult(
    override val expression: Fx,
    override val methodName: String,
    override val iterations: List<BracketIteration>,
    override val terminationCause: TerminationCause,
) : IterationResult() {


    override fun getEquationString(xLast: RoundedDecimal) = "$expression = 0"

    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xL, xR, yL, yR, xNew, yNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        stringBuilder.append(terminationCause.message)
        return stringBuilder.toString()
    }

}

data class BracketIteration(
    val xL: RoundedDecimal,
    val xR: RoundedDecimal,
    val yL: RoundedDecimal,
    val yR: RoundedDecimal,
    override val xNew: RoundedDecimal,
    val yNew: RoundedDecimal,
    override val error: Percentage,
) : Iteration() {

    /**
     * Writes the iteration into a single-line CSV string
     * It follows the format "xL, xR, yL, yR, xNew, yNew, error"
     */
    override fun toString() = "$xL, $xR, $yL, $yR, $xNew, $yNew, $error"
}

fun runIterativeBracketing(
    f: Fx,
    methodName: String,
    initialInterval: BracketInterval,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
    xNFormula: (xL: BigDecimal, xR: BigDecimal, yL: BigDecimal, yR: BigDecimal) -> BigDecimal,
): BracketIterationResult {

    var iterator = 0
    val iterations = mutableListOf<BracketIteration>()

    var xL = initialInterval.xL
    var xR = initialInterval.xR

    var xOld: BigDecimal? = null

    var error = getMaxError(scale, roundingMode)

    while (true) {

        if (iterator >= minIterations && error.value.isZero) {
            return BracketIterationResult(f, methodName, iterations, TerminationCause.ZeroErrorReached)
        } else if (iterator >= maxIterations) {
            return BracketIterationResult(f, methodName, iterations, TerminationCause.MaxIterationsReached)
        }

        val yL = f.calculate(xL, scale, roundingMode)
        val yR = f.calculate(xR, scale, roundingMode)

        if (BigDecimal.ZERO !in yL..yR && BigDecimal.ZERO !in yR..yL) throw NoZeroInIntervalException(xL, xR, yL, yR)

        val xNew = xNFormula(xL, xR, yL, yR)
        val yNew = f.calculate(xNew, scale, roundingMode)

        error = calculateError(
            xOld = xOld,
            xNew = xNew,
            scale = scale,
            roundingMode = roundingMode
        )

        iterations.add(
            BracketIteration(
                xL = xL.round(scale, roundingMode),
                xR = xR.round(scale, roundingMode),
                yL = yL.round(scale, roundingMode),
                yR = yR.round(scale, roundingMode),
                xNew = xNew.round(scale, roundingMode),
                yNew = yNew.round(scale, roundingMode),
                error = error,
            )
        )

        xOld = xNew

        if (BigDecimal.ZERO in yL..yNew || BigDecimal.ZERO in yNew..yL) xR = xNew
        else xL = xNew

        iterator++

    }

}