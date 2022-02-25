package methods.common

import java.math.RoundingMode

const val DEFAULT_CALCULATION_SCALE = 64
const val DEFAULT_OUTPUT_SCALE = 4
val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP

fun getMaxError(scale: Int, roundingMode: RoundingMode) =
    1.toBigDecimal().toPercentage(scale, roundingMode)