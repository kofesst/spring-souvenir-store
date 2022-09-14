package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.Employee
import me.kofesst.spring.souvenirstore.model.UserRole
import me.kofesst.spring.souvenirstore.util.YearsDifference
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.validation.constraints.*

data class EmployeeForm(
    private val id: Long = 0,
    var userId: Long = 0,

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

    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull(message = "Это обязательное поле")
    @field:Past(message = "Некорректная дата")
    @field:YearsDifference(minYears = 18, message = "Сотруднику должно быть не менее 18 лет")
    var dateOfBirth: Date? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:Positive(message = "Число должно быть положительным")
    @field:Max(value = 1_000_000_000)
    var salary: Int? = null,

    @field:NotNull(message = "Это обязательное поле")
    var role: String? = null,
) {
    companion object {
        fun fromModel(model: Employee) = with(model) {
            EmployeeForm(
                id = id,
                userId = user.id,
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                dateOfBirth = dateOfBirth,
                salary = salary,
                role = user.role.authority
            )
        }
    }

    fun toModel() = Employee(
        id = id,
        firstName = firstName!!,
        lastName = lastName!!,
        middleName = middleName!!,
        dateOfBirth = dateOfBirth!!,
        salary = salary!!
    )

    fun getRole() = UserRole.valueOf(role!!)
}