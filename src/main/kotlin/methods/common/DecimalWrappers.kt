package methods.common

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.reflect.KProperty

class RoundedDecimal(value: BigDecimal, val scale: Int, val roundingMode: RoundingMode) {
    val value = value.setScale(scale, roundingMode)
    val isZero: Boolean
        get() = toString() == BigDecimal.ZERO.round(scale, roundingMode).toString()

    operator fun getValue(thisRef: RoundedDecimal, property: KProperty<*>): BigDecimal = value
    override fun toString() = value.toString()
}

fun BigDecimal.round(scale: Int, roundingMode: RoundingMode) = RoundedDecimal(this, scale, roundingMode)

class Percentage (decimalValue: BigDecimal, scale: Int, roundingMode: RoundingMode) {
    val value = (decimalValue * 100.toBigDecimal()).round(scale, roundingMode)

    operator fun getValue(thisRef: RoundedDecimal, property: KProperty<*>): BigDecimal = value.value
    override fun toString() = "$value%"
}

fun BigDecimal.toPercentage(scale: Int, roundingMode: RoundingMode) = Percentage(this, scale, roundingMode)