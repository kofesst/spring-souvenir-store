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

    @Column(name = "date_of_birth")
    val dateOfBirth: Date = Date(),

    @Column(name = "salary", nullable = false)
    val salary: Int = 0,

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    val user: UserDto = UserDto(),
) : BaseDto<Employee> {
    companion object {
        fun fromModel(model: Employee) = with(model) {
            EmployeeDto(
                id = id,
                dateOfBirth = dateOfBirth,
                salary = salary,
                user = UserDto.fromModel(user)
            )
        }
    }

    override fun toModel() = Employee(
        id = id,
        dateOfBirth = dateOfBirth,
        salary = salary,
        user = user.toModel()
    )
}