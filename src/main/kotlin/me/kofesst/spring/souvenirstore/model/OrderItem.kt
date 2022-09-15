package me.kofesst.spring.souvenirstore.model

data class OrderItem(
    val id: Long = 0,
    val count: Int = 0,
    val product: Product = Product(),
    val order: CustomerOrder = CustomerOrder(),
)
