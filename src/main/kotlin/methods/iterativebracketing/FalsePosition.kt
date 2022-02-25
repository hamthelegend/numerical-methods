package methods.iterativebracketing

import methods.common.Default
import methods.common.Fx
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Uses the false position method to approximate a solution of a function
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 *
 * @return is the list of all the false position iterations
 */
fun Fx.runFalsePosition(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    minIterations: Int,
    maxIterations: Int,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
) =
    runIterativeBracketing(
        methodName = "False Position",
        initialXL = initialXL,
        initialXR = initialXR,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode
    ) { xL, xR, yL, yR ->
        (xL + (xR - xL) * (yL.divide(yL - yR, scale, roundingMode))).setScale(scale, roundingMode)
    }