package me.kofesst.spring.souvenirstore.model

data class Position(
    val id: Long = 0,
    var title: String = "",
    var salary: Int = 0,
) {
    override fun toString(): String {
        return "Оклад: $salary руб."
    }
}
