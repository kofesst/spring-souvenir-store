package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.PromoCode
import javax.validation.constraints.*

data class PromoCodeForm(
    val id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 255, message = "Максимальная длина - 255 символов")
    var code: String? = null,

    var active: Boolean = true,

    @field:NotNull(message = "Это обязательное поле")
    @field:Min(value = 1, message = "Процент скидки должен быть больше 1%")
    @field:Max(value = 100, message = "Процент скидки должен быть меньше 100%")
    var percent: Double? = null,
) {
    companion object {
        fun fromModel(model: PromoCode) = with(model) {
            PromoCodeForm(
                id = id,
                code = code,
                active = active,
                percent = percent
            )
        }
    }

    fun toModel() = PromoCode(
        id = id,
        code = code!!,
        active = active,
        percent = percent!!
    )
}