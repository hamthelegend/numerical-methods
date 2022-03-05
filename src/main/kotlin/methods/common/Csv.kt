package methods.common

data class Csv<T>(val values: List<T>) {

    constructor(vararg values: T): this(values.toList())

    override fun toString(): String {
        val csvBuilder = StringBuilder()
        for (value in values) {
            csvBuilder.append("${value ?: "N/A"}, ")
        }
        return csvBuilder.toString()
    }

}
