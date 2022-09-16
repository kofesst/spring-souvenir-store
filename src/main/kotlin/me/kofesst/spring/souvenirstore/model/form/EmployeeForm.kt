package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.Employee
import me.kofesst.spring.souvenirstore.util.YearsDifference
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Positive

data class EmployeeForm(
    private val id: Long = 0,

    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull(message = "Это обязательное поле")
    @field:Past(message = "Некорректная дата")
    @field:YearsDifference(minYears = 18, message = "Сотруднику должно быть не менее 18 лет")
    var dateOfBirth: Date? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:Positive(message = "Число должно быть положительным")
    @field:Max(value = 1_000_000_000)
    var salary: Int? = null,
) {
    companion object {
        fun fromModel(model: Employee) = EmployeeForm(
            id = model.id,
            dateOfBirth = model.dateOfBirth,
            salary = model.salary
        )
    }

    fun toModel() = Employee(
        id = id,
        dateOfBirth = dateOfBirth!!,
        salary = salary!!
    )
}