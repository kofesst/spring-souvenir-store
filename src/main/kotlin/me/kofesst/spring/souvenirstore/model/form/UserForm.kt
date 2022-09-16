package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.User
import me.kofesst.spring.souvenirstore.model.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class UserForm(
    var id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 15, message = "Максимальная длина - 30 символов")
    @field:Pattern(regexp = "^.*[a-zA-Z].*$", message = "Пароль должен содержать хотя бы одну букву")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин не должен содержать ничего кроме букв и цифр")
    var login: String? = "invalidLogin",

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 30, message = "Максимальная длина - 30 символов")
    @field:Pattern(regexp = "^.*\\d.*$", message = "Пароль должен содержать хотя бы одну цифру")
    @field:Pattern(regexp = "^.*[a-z].*$", message = "Пароль должен содержать хотя бы одну строчную букву")
    @field:Pattern(regexp = "^.*[A-Z].*$", message = "Пароль должен содержать хотя бы одну прописную букву")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Пароль не должен содержать ничего кроме букв и цифр")
    var password: String? = "invalidPassword123",

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 30, message = "Максимальная длина - 30 символов")
    var firstName: String? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 50, message = "Максимальная длина - 50 символов")
    var lastName: String? = null,

    @field:Size(max = 50, message = "Максимальная длина - 50 символов")
    var middleName: String? = null,

    @field:NotNull(message = "Это обязательное поле")
    var role: String? = null,
) {
    companion object {
        fun fromModel(model: User) = with(model) {
            UserForm(
                id = id,
                login = login,
                password = password,
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                role = role.authority
            )
        }
    }

    fun toModel(passwordEncoder: PasswordEncoder) = User(
        id = id,
        login = login!!,
        password = passwordEncoder.encode(password!!),
        firstName = firstName!!,
        lastName = lastName!!,
        middleName = middleName!!,
        role = UserRole.valueOf(role!!)
    )

    fun toModel(existing: User) = existing.copy(
        firstName = firstName!!,
        lastName = lastName!!,
        middleName = middleName!!,
        role = UserRole.valueOf(role!!)
    )
}
