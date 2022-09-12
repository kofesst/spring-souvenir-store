package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.User
import me.kofesst.spring.souvenirstore.model.UserRole
import javax.persistence.*

@Entity
@Table(name = "user")
data class UserDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "login", unique = true, nullable = false, length = 15)
    val login: String = "",

    @Column(name = "password", nullable = false, length = 255)
    val password: String = "",

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole = UserRole.User,
) : BaseDto<User> {
    companion object {
        fun fromModel(model: User) = with(model) {
            UserDto(
                id = id,
                login = login,
                password = password,
                role = role
            )
        }
    }

    override fun toModel() = User(
        id = id,
        login = login,
        password = password,
        role = role
    )
}
