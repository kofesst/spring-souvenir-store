package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.PickupPoint
import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "pickup_point")
data class PickupPointDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_point", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "address", unique = true, nullable = false)
    val address: String = "",

    @Column(name = "start_time", nullable = false)
    val startTime: Time = Time.valueOf("00:00:00"),

    @Column(name = "end_time", nullable = false)
    val endTime: Time = Time.valueOf("23:59:59"),
) : BaseDto<PickupPoint> {
    companion object {
        fun fromModel(model: PickupPoint) = with(model) {
            PickupPointDto(
                id = id,
                address = address,
                startTime = startTime,
                endTime = endTime,
            )
        }
    }

    override fun toModel() = PickupPoint(
        id = id,
        address = address,
        startTime = startTime,
        endTime = endTime
    )
}