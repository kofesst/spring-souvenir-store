package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Customer
import javax.persistence.*

@Entity
@Table(name = "customer")
data class CustomerDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "points", nullable = false)
    val points: Int = 0,

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    val user: UserDto = UserDto(),

    @ManyToMany
    @JoinTable(
        name = "marked_point",
        joinColumns = [JoinColumn(name = "customer_id")],
        inverseJoinColumns = [JoinColumn(name = "point_id")]
    )
    val markedPoints: List<PickupPointDto> = emptyList(),

    @ManyToMany
    @JoinTable(
        name = "marked_product",
        joinColumns = [JoinColumn(name = "customer_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    val markedProducts: List<ProductDto> = emptyList()
) : BaseDto<Customer> {
    companion object {
        fun fromModel(model: Customer) = with(model) {
            CustomerDto(
                id = id,
                points = points,
                user = UserDto.fromModel(user)
            )
        }
    }

    override fun toModel(): Customer = Customer(
        id = id,
        points = points,
        user = user.toModel()
    )
}