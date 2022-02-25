package methods.common

import java.math.RoundingMode

object Default {
    const val SCALE = 6
    val ROUNDING_MODE = RoundingMode.HALF_EVEN
}

fun getMaxError(scale: Int, roundingMode: RoundingMode) =
    1.toBigDecimal().toPercentage(scale, roundingMode)