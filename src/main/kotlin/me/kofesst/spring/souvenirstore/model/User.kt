package me.kofesst.spring.souvenirstore.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long = 0,
    val login: String = "",
    private val password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    val middleName: String? = null,
    val role: UserRole = UserRole.User,
) : UserDetails {
    val fullName: String
        get() = buildString {
            append("$lastName $firstName")
            if (middleName != null) {
                append(middleName)
            }
        }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(role)

    override fun getPassword(): String = password

    override fun getUsername(): String = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}