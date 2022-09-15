package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.PromoCode
import javax.persistence.*

@Entity
@Table(name = "promo_code")
data class PromoCodeDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_code", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "code", unique = true, nullable = false)
    val code: String = "",

    @Column(name = "active", nullable = false)
    val active: Boolean = false,

    @Column(name = "percent", nullable = false)
    val percent: Double = 0.0,
) : BaseDto<PromoCode> {
    companion object {
        fun fromModel(model: PromoCode) = with(model) {
            PromoCodeDto(
                id = id,
                code = code,
                active = active,
                percent = percent
            )
        }
    }

    override fun toModel() = PromoCode(
        id = id,
        code = code,
        active = active,
        percent = percent
    )
}
