package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Position
import javax.persistence.*

@Entity
@Table(name = "position")
data class PositionDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_position", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "title", unique = true, length = 30, nullable = false)
    val title: String = "",

    @Column(name = "salary", nullable = false)
    val salary: Int = 0,
) : BaseDto<Position> {
    companion object {
        fun fromModel(model: Position) = with(model) {
            PositionDto(
                id = id,
                title = title,
                salary = salary
            )
        }
    }

    override fun toModel() = Position(
        id = id,
        title = title,
        salary = salary
    )
}
