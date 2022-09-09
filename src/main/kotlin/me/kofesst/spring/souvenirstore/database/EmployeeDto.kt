package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Employee
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "employee")
data class EmployeeDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "first_name", length = 30, nullable = false)
    val firstName: String = "",

    @Column(name = "last_name", length = 50, nullable = false)
    val lastName: String = "",

    @Column(name = "middle_name", length = 30, nullable = true)
    val middleName: String = "",

    @Column(name = "date_of_birth")
    val dateOfBirth: Date = Date(),

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id_position")
    val position: PositionDto = PositionDto(),
) : BaseDto<Employee> {
    companion object {
        fun fromModel(model: Employee) = with(model) {
            EmployeeDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                dateOfBirth = dateOfBirth,
                position = PositionDto.fromModel(position)
            )
        }
    }

    override fun toModel() = Employee(
        id = id,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        dateOfBirth = dateOfBirth,
        position = position.toModel()
    )
}