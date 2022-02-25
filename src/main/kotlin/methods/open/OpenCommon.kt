package methods.open

import methods.common.Default
import methods.common.Fx
import methods.common.calculate
import java.math.BigDecimal
import java.math.RoundingMode

fun Fx.getInitialX(
    guessX: BigDecimal = BigDecimal.ZERO,
    calculationScale: Int = Default.SCALE,
    roundingMode: RoundingMode = Default.ROUNDING_MODE,
): BigDecimal = try {
    calculate(guessX, calculationScale, roundingMode)
    guessX
} catch (e: ArithmeticException) {
    val newGuessX = if (guessX > BigDecimal.ZERO) -guessX else guessX + BigDecimal.ONE
    getInitialX(newGuessX, calculationScale, roundingMode)
}