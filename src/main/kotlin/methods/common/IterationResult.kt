package methods.common

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

abstract class IterationResult {
    abstract val expression: Fx
    abstract val methodName: String
    abstract val iterations: List<Iteration>

    abstract fun getEquationString(xLast: RoundedDecimal): String

    fun writeFile() {
        val xLast = iterations.last().xNew
        println("$methodName: x ≈ $xLast for ${getEquationString(xLast)}")
        val outputDirectoryName = "output"
        val outputDirectory = File(outputDirectoryName)
        if (!outputDirectory.exists()) outputDirectory.mkdir()
        val fileName = "$outputDirectoryName/$methodName.csv"
        val file = File(fileName)
        file.writeText(toString())
        println("Answer written to $fileName\n")
    }

}

abstract class Iteration {
    abstract val xNew: RoundedDecimal
    abstract val error: Percentage
}

fun calculateError(xOld: BigDecimal, xNew: BigDecimal, scale: Int, roundingMode: RoundingMode) =
    xOld.minus(xNew).divide(xNew, scale, roundingMode).abs() // ((xNew - xOld) / xNew).abs()