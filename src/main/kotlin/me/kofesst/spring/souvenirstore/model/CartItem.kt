package me.kofesst.spring.souvenirstore.model

data class CartItem(
    val id: Long = 0,
    val product: Product = Product(),
    var count: Int = 0,
) {
    val price: Double
        get() = product.price * count

    val oldPrice: Double?
        get() = product.oldPrice?.times(count)

    fun toOrderItem() = OrderItem(
        count = count,
        product = product
    )
}