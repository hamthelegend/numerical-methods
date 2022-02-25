package methods.common

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

enum class TerminationCause(val message: String) {
    MaxIterationsReached("Maximum iterations reached without reaching an absolute relative approximate error of zero."),
    ZeroErrorReached("Absolute relative approximate error of zero and minimum iterations reached."),
}

abstract class IterationResult {
    abstract val expression: Fx
    abstract val methodName: String
    abstract val iterations: List<Iteration>
    abstract val terminationCause: TerminationCause

    abstract fun getEquationString(xLast: RoundedDecimal): String

    fun writeFile() {
        val xLast = iterations.last().xNew
        println("$methodName: x ≈ $xLast for ${getEquationString(xLast)}")
        println(terminationCause.message)

        val outputDirectoryName = "output"
        val outputDirectory = File(outputDirectoryName)
        if (!outputDirectory.exists()) outputDirectory.mkdir()
        val fileName = "$outputDirectoryName/$methodName.csv"
        val file = File(fileName)
        file.delete()
        file.writeText(toString())
        println("Answer written to $fileName\n")
    }

}

abstract class Iteration {
    abstract val xNew: RoundedDecimal
    abstract val error: Percentage
}

fun calculateError(
    xOld: BigDecimal?,
    xNew: BigDecimal,
    calculationScale: Int,
    outputScale: Int,
    roundingMode: RoundingMode,
) =
    // ((xNew - xOld) / xNew).abs()
    xOld?.minus(xNew)?.divide(xNew, calculationScale, roundingMode)?.abs()?.toPercentage(outputScale, roundingMode)
        ?: 1.toBigDecimal().toPercentage(outputScale, roundingMode)