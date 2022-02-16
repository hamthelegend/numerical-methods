package methods.iterativebracketing

import org.mariuszgromada.math.mxparser.Expression

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
fun Expression.bisection(initialXL: Double, initialXR: Double, numberOfIterations: Int) =
    iterativeBracketing(initialXL, initialXR, numberOfIterations) { xL, xR, _, _ ->
        (xL + xR) / 2
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
fun Expression.writeBisection(initialXL: Double, initialXR: Double, numberOfIterations: Int) {
    writeFile(
        initialXL = initialXL,
        initialXR = initialXR,
        numberOfIterations = numberOfIterations,
        methodName = "Bisection",
    ) { xL, xR, i ->
        bisection(xL, xR, i)
    }
}