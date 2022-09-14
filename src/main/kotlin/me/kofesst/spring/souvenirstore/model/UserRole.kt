package me.kofesst.spring.souvenirstore.model

import org.springframework.security.core.GrantedAuthority

enum class UserRole(val position: Boolean, val displayName: String) : GrantedAuthority {
    User(position = false, displayName = "Пользователь"),
    HR(position = true, displayName = "Кадровик"),
    Cashier(position = true, displayName = "Кассир"),
    Accountant(position = true, displayName = "Бухгалтер"),
    Director(position = true, displayName = "Директор");

    companion object {
        fun positions() = UserRole.values().filter { it.position }
    }

    override fun getAuthority(): String = this.name
}