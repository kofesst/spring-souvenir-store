package me.kofesst.spring.souvenirstore.model

data class Customer(
    val id: Long = 0,
    val points: Int = 0,
    val user: User = User(),
)