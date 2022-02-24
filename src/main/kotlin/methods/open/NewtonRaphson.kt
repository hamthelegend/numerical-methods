package methods.open

import methods.*
import java.math.BigDecimal
import java.math.RoundingMode

data class NewtonRaphsonIterationResult(
    override val iterations: List<NewtonRaphsonIteration>,
) : IterationResult() {
    override val methodName = "Newton Raphson"
    override fun toString(): String {
        val stringBuilder = StringBuilder("i, xOld, fXOld, fPrimeXOld, xNew, error\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, $iteration\n")
        }
        return stringBuilder.toString()
    }
}

data class NewtonRaphsonIteration(
    override val xOld: BigDecimal,
    val fXOld: BigDecimal,
    val fPrimeXOld: BigDecimal,
    override val xNew: BigDecimal,
    override val scale: Int,
    override val roundingMode: RoundingMode,
) : Iteration() {
    override fun toString() = "$xOld, $fXOld, $fPrimeXOld, $xNew, $error"
}

fun Fx.runNewtonRaphson(
    derivative: Fx,
    numberOfIterations: Int,
    initialX: BigDecimal = guessInitialX(),
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): NewtonRaphsonIterationResult {

    val iterations = mutableListOf<NewtonRaphsonIteration>()
    var xOld = initialX
    repeat(numberOfIterations) {
        val fXOld = calculate(xOld, calculationScale, roundingMode)
        val fPrimeXOld = derivative.calculate(xOld, calculationScale, roundingMode)
        val xNew = xOld - fXOld / fPrimeXOld
        iterations.add(
            NewtonRaphsonIteration(
                xOld = xOld,
                fXOld = fXOld,
                fPrimeXOld = fPrimeXOld,
                xNew = xNew,
                scale = outputScale,
                roundingMode = roundingMode
            )
        )
        xOld = xNew
    }
    return NewtonRaphsonIterationResult(iterations)

}