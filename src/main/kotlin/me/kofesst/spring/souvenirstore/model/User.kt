package me.kofesst.spring.souvenirstore.model

data class User(
    val id: Long = 0,
    val login: String = "",
    val password: String = "",
)
