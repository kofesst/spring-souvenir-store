package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.CustomerOrder
import me.kofesst.spring.souvenirstore.model.OrderStatus
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "customer_order")
data class CustomerOrderDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "order_date", nullable = false)
    val orderDate: Date = Date(),

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: OrderStatus = OrderStatus.Preparing,

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: CustomerDto = CustomerDto(),

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    val point: PickupPointDto = PickupPointDto(),

    @ManyToOne
    @JoinColumn(name = "code_id")
    val code: PromoCodeDto? = null,

    @OneToMany(cascade = [CascadeType.MERGE])
    val items: List<OrderItemDto> = emptyList(),
) : BaseDto<CustomerOrder> {
    companion object {
        fun fromModel(model: CustomerOrder) = with(model) {
            CustomerOrderDto(
                id = id,
                orderDate = orderDate,
                status = status,
                customer = CustomerDto.fromModel(customer),
                point = PickupPointDto.fromModel(point),
                code = if (code != null) {
                    PromoCodeDto.fromModel(code)
                } else {
                    null
                }
            )
        }
    }

    override fun toModel() = CustomerOrder(
        id = id,
        orderDate = orderDate,
        status = status,
        customer = customer.toModel(),
        point = point.toModel(),
        code = code?.toModel(),
        items = items.map { it.toModel() }
    )
}
