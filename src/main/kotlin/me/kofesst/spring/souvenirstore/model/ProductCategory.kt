package me.kofesst.spring.souvenirstore.model

data class ProductCategory(
    val id: Long = 0,
    val displayName: String = "",
    val products: List<Product> = emptyList(),
)