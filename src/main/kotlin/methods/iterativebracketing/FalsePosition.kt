package methods.iterativebracketing

import methods.DIVISION_ROUNDING_SCALE
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
 *
 * @return is the list of all the false position iterations
 */
fun Expression.falsePosition(initialXL: BigDecimal, initialXR: BigDecimal, numberOfIterations: Int) =
    iterativeBracketing(initialXL, initialXR, numberOfIterations) { xL, xR, yL, yR ->
        xL + (xR - xL) * (yL.divide(yL - yR, DIVISION_ROUNDING_SCALE, RoundingMode.HALF_EVEN))
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