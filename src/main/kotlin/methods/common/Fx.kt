package methods.common

import com.mpobjects.bdparsii.eval.Parser
import com.mpobjects.bdparsii.eval.Scope
import java.math.BigDecimal
import java.math.RoundingMode

data class Fx(val expression: String) {
    override fun toString() = expression
}

fun Fx.calculate(
    x: BigDecimal,
    scale: Int = Default.SCALE,
    roundingMode: RoundingMode,
): BigDecimal {
    val scope = Scope()
    val xVariable = scope.getVariable("x")
    val expression = Parser.parse(expression, scope)
    xVariable.value = x
    return expression.evaluate().setScale(scale, roundingMode)
}