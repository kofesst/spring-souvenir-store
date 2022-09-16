package me.kofesst.spring.souvenirstore.model

import me.kofesst.spring.souvenirstore.util.yearsUntil
import java.util.*

data class Employee(
    val id: Long = 0,
    var dateOfBirth: Date = Date(),
    val salary: Int = 0,
    var user: User = User(),
) {
    val age: Int get() = dateOfBirth yearsUntil Date()

    override fun toString(): String {
        return "Должность: ${user.role.displayName}\nЗарплата: $salary руб.\nВозраст: $age"
    }
}
