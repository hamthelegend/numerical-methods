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
    abstract val columnNamesCsv: Csv<String>

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
        file.writeText(toCsvString())
        println("Answer written to $fileName\n")
    }

    fun toCsvString(): String {
        val stringBuilder = StringBuilder("$columnNamesCsv\n")
        for ((i, iteration) in iterations.withIndex()) {
            stringBuilder.append("${i + 1}, ${iteration.valuesCsv}\n")
        }
        stringBuilder.append(terminationCause.message)
        return stringBuilder.toString()
    }

}

abstract class Iteration {
    abstract val xNew: RoundedDecimal
    abstract val error: Percentage?
    abstract val valuesCsv: Csv<Any?>
}

fun calculateError(
    xOld: BigDecimal?,
    xNew: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode,
) =
    // ((xNew - xOld) / xNew).abs()
    xOld?.minus(xNew)?.divide(xNew, scale, roundingMode)?.abs()?.toPercentage(scale, roundingMode)