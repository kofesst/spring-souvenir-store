package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.Position
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class PositionForm(
    private val id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 30, message = "Максимальная длина - 30 символов")
    var title: String? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:Positive(message = "Число должно быть положительным")
    @field:Max(value = 1_000_000_000)
    var salary: Int? = null,
) {
    companion object {
        fun fromModel(model: Position) = with(model) {
            PositionForm(
                id = id,
                title = title,
                salary = salary
            )
        }
    }

    fun toModel() = Position(
        id = id,
        title = title!!,
        salary = salary!!
    )
}