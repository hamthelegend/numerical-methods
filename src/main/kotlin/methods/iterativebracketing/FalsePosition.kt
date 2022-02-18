package methods.iterativebracketing

import org.mariuszgromada.math.mxparser.Expression

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
fun Expression.falsePosition(initialXL: Double, initialXR: Double, numberOfIterations: Int) =
    iterativeBracketing(initialXL, initialXR, numberOfIterations) { xL, xR, yL, yR ->
        xL + (xR - xL) * (yL / (yL - yR))
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
fun Expression.writeFalsePosition(
    initialXL: Double,
    initialXR: Double,
    numberOfIterations: Int,
) {
    falsePosition(initialXL, initialXR, numberOfIterations).writeFile(methodName = "FalsePosition")
}