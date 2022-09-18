package me.kofesst.spring.souvenirstore.model

data class Product(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val oldPrice: Double? = null,
    val stockAmount: Int = 0,
    var category: ProductCategory = ProductCategory(),
)
