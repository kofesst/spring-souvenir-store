package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.ProductCategory
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProductCategoryForm(
    private val id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 30, message = "Максимальная длина - 30 символов")
    val displayName: String? = null,
) {
    companion object {
        fun fromModel(model: ProductCategory) = with(model) {
            ProductCategoryForm(
                id = id,
                displayName = displayName
            )
        }
    }

    fun toModel() = ProductCategory(
        id = id,
        displayName = displayName!!
    )
}