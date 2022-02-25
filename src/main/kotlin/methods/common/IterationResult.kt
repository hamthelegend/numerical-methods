package methods.common

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

abstract class IterationResult {
    abstract val methodName: String
    abstract val iterations: List<Iteration>

//    abstract fun getFunctionString(xLast: )

    fun writeFile() {
        println("$methodName: x ≈ ${iterations.last().xNew}")
        val fileName = "$methodName.csv"
        val file = File(fileName)
        file.writeText(toString())
        println("Answer written to $fileName")
    }

}

abstract class Iteration {
    abstract val xNew: RoundedDecimal
    abstract val scale: Int
    abstract val roundingMode: RoundingMode
    abstract val error: Percentage
}

fun calculateError(xOld: BigDecimal, xNew: BigDecimal, scale: Int, roundingMode: RoundingMode) =
    xOld.minus(xNew).divide(xNew, scale, roundingMode).abs() // ((xNew - xOld) / xNew).abs()