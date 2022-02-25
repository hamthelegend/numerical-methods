package methods.iterativebracketing

import methods.common.Default
import methods.common.Fx
import java.math.RoundingMode

fun runBisection(
    f: Fx,
    initialInterval: BracketInterval,
    minIterations: Int = Default.MIN_ITERATION,
    maxIterations: Int = Default.MAX_ITERATION,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
) =
    runIterativeBracketing(
        f,
        methodName = "Bisection",
        initialInterval = initialInterval,
        minIterations = minIterations,
        maxIterations = maxIterations,
        scale = scale,
        roundingMode = roundingMode,
    ) { xL, xR, _, _ ->
        (xL + xR).divide(2.toBigDecimal(), scale, roundingMode)
    }