package methods.iterativebracketing

import methods.common.Default
import methods.common.Fx
import java.math.RoundingMode

fun runFalsePosition(
    f: Fx,
    initialInterval: BracketInterval,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
) =
    runIterativeBracketing(
        f,
        methodName = "False Position",
        initialInterval = initialInterval,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode
    ) { xL, xR, yL, yR ->
        (xL + (xR - xL) * (yL.divide(yL - yR, scale, roundingMode))).setScale(scale, roundingMode)
    }