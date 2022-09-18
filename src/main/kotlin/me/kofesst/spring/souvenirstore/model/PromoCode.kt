package me.kofesst.spring.souvenirstore.model

data class PromoCode(
    val id: Long = 0,
    val code: String = "",
    val active: Boolean = false,
    val percent: Double = 0.0,
) {
    override fun toString(): String {
        return "Скидка $percent%"
    }
}