package me.kofesst.spring.souvenirstore.model

import me.kofesst.spring.souvenirstore.util.yearsUntil
import java.util.*

data class Employee(
    val id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    val middleName: String = "",
    var dateOfBirth: Date = Date(),
    val salary: Int = 0,
    var user: User = User(),
) {
    val fullName: String get() = "$lastName $firstName $middleName"
    val age: Int get() = dateOfBirth yearsUntil Date()

    override fun toString(): String {
        return "Зарплата: $salary руб.\nВозраст: $age"
    }
}
