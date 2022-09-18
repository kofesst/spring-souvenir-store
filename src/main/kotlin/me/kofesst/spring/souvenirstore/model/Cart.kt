package me.kofesst.spring.souvenirstore.model

data class Cart(
    val id: Long = 0,
    val customer: Customer = Customer(),
    val items: MutableList<CartItem> = mutableListOf(),
    var code: PromoCode? = null,
) {
    val size: Int
        get() = items.sumOf { it.count }

    val itemsPrice: Double
        get() = items.sumOf { it.product.price * it.count }

    val totalPrice: Double
        get() = itemsPrice - itemsPrice * (code?.percent ?: 0.0) / 100

    fun addProduct(product: Product): CartItem {
        val existing = items.indexOfFirst { it.product.id == product.id }
        return if (existing != -1) {
            items[existing].copy(
                count = items[existing].count + 1
            )
        } else {
            CartItem(
                product = product,
                count = 1
            )
        }
    }
}