package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.Product
import javax.validation.constraints.*

data class ProductForm(
    private val id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 30, message = "Максимальная длина - 30 символов")
    val title: String? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 255, message = "Максимальная длина - 255 символов")
    val description: String? = null,

    @field:NotNull(message = "Это обязательное поле")
    @field:Positive(message = "Число должно быть положительным")
    @field:Max(value = 1_000_000_000, message = "Слишком большое число")
    val price: Double? = null,
) {
    companion object {
        fun fromModel(model: Product) = with(model) {
            ProductForm(
                id = id,
                title = title,
                description = description,
                price = price
            )
        }
    }

    fun toModel() = Product(
        id = id,
        title = title!!,
        description = description!!,
        price = price!!
    )
}