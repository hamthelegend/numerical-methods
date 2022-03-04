package methods.common

data class Csv(val values: List<String>) {

    constructor(vararg values: Any): this(values.toList().map { it.toString() })

    override fun toString(): String {
        val csvBuilder = StringBuilder()
        for (value in values) {
            csvBuilder.append("$value, ")
        }
        return csvBuilder.toString()
    }

}
