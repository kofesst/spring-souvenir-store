package me.kofesst.spring.souvenirstore.model

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    User, HR, Cashier, Accountant, Director;

    companion object {
        @Deprecated(
            message = "В дальнейшем админу запретят добавлять и удалять должности, и код может измениться.",
            replaceWith = ReplaceWith("")
        )
        fun getFromPosition(position: Position) = when (position.id) {
            26L -> Director
            28L -> Cashier
            30L -> HR
            31L -> Accountant
            else -> User
        }
    }

    override fun getAuthority(): String = this.name
}