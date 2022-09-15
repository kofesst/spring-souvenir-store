package me.kofesst.spring.souvenirstore.model

enum class OrderStatus(val displayName: String) {
    Preparing(displayName = "Собирается"),
    OnTheWay(displayName = "В пути"),
    Delivered(displayName = "Доставлен"),
    Completed(displayName = "Получен")
}