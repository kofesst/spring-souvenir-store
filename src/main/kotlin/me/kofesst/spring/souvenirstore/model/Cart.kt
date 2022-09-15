package me.kofesst.spring.souvenirstore.model

data class Cart(
    val rowId: String = "",
    val customer: Customer = Customer(),
    val product: Product = Product(),
    val count: Int = 0,
)
