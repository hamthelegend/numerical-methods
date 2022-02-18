package methods.iterativebracketing

import methods.DEFAULT_ROUNDING_MODE
import methods.DEFAULT_SCALE
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Uses the false position method to approximate a solution of a function
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 * @param scale is the scale that the BigDecimal will use
 * @param roundingMode is the RoundingMode that the BigDecimal will use
 *
 * @return is the list of all the false position iterations
 */
fun Expression.falsePosition(
    initialXL: BigDecimal,
    initialXR: BigDecimal,
    numberOfIterations: Int,
    scale: Int = DEFAULT_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
) =
    iterativeBracketing(initialXL, initialXR, numberOfIterations) { xL, xR, yL, yR ->
        (xL + (xR - xL) * (yL / (yL - yR).setScale(scale, roundingMode))).setScale(scale, roundingMode)
    }

/**
 * Uses the false-position method to approximate a solution of a function and writes the iterations in a .csv file
 *
 * @receiver is the expression that you want to solve; f(x) = [this]
 *
 * @param initialXL is the left x of your interval
 * @param initialXR is the right x of your interval
 * @param numberOfIterations is the number of times you want to run the algorithm
 */
fun Expression.writeFalsePosition(initialXL: BigDecimal, initialXR: BigDecimal, numberOfIterations: Int) {
    writeFile(
        initialXL = initialXL,
        initialXR = initialXR,
        numberOfIterations = numberOfIterations,
        methodName = "FalsePosition",
    ) { xL, xR, i ->
        falsePosition(xL, xR, i)
    }
}