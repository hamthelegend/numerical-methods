package methods.open

import methods.DEFAULT_CALCULATION_SCALE
import methods.DEFAULT_ROUNDING_MODE
import methods.Fx
import methods.calculate
import java.math.BigDecimal
import java.math.RoundingMode

fun Fx.guessInitialX(
    guessX: BigDecimal = BigDecimal.ZERO,
    calculationScale: Int = DEFAULT_CALCULATION_SCALE,
    roundingMode: RoundingMode = DEFAULT_ROUNDING_MODE,
): BigDecimal = try {
    calculate(guessX, calculationScale, roundingMode)
    guessX
} catch (e: ArithmeticException) {
    val newGuessX = if (guessX > BigDecimal.ZERO) -guessX else guessX + BigDecimal.ONE
    guessInitialX(newGuessX, calculationScale, roundingMode)
}