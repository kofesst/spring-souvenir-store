package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.PickupPoint
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Time
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PickupPointForm(
    val id: Long = 0,

    @field:NotNull(message = "Это обязательное поле")
    @field:NotBlank(message = "Поле не может быть пустым")
    @field:Size(max = 255, message = "Максимальная длина - 255 символов")
    var address: String? = null,

    @field:DateTimeFormat(pattern = "HH:mm")
    @field:NotNull(message = "Это обязательное поле")
    var startTime: Date? = null,

    @field:DateTimeFormat(pattern = "HH:mm")
    @field:NotNull(message = "Это обязательное поле")
    var endTime: Date? = null,
) {
    companion object {
        fun fromModel(model: PickupPoint) = with(model) {
            PickupPointForm(
                id = id,
                address = address,
                startTime = startTime,
                endTime = endTime
            )
        }
    }

    fun toModel() = PickupPoint(
        id = id,
        address = address!!,
        startTime = Time(startTime!!.time),
        endTime = Time(endTime!!.time)
    )
}