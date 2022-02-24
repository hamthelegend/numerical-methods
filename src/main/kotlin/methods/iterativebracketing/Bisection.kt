package methods.iterativebracketing

import methods.DEFAULT_OUTPUT_SCALE
import methods.DEFAULT_ROUNDING_MODE
import methods.DEFAULT_SCALE
import methods.Fx
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Uses the bisection method to approximate a solution of a function
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 *
 * @return is the list of all the bisection iterations
 */
fun Fx.bisection(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    scale: Int = DEFAULT_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
) =
    iterativeBracketing(
        initialXL = initialXL,
        initialXR = initialXR,
        numberOfIterations = numberOfIterations,
        calculationScale = scale,
        outputScale = outputScale,
        roundingMode = roundingMode,
    ) { xL, xR, _, _ ->
        (xL + xR).divide(2.toBigDecimal(), scale, roundingMode)
    }


/**
 * Uses the bisection method to approximate a solution of a function and writes the iterations in a .csv file
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 */
fun Fx.writeBisection(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    scale: Int = DEFAULT_SCALE,
    outputScale: Int = DEFAULT_OUTPUT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
) {
    bisection(
        initialXL = initialXL,
        initialXR = initialXR,
        numberOfIterations = numberOfIterations,
        scale = scale,
        outputScale = outputScale,
        roundingMode = roundingMode
    ).writeFile(methodName = "Bisection")
}