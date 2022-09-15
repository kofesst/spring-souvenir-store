package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.OrderItem
import javax.persistence.*

@Entity
@Table(name = "order_item")
data class OrderItemDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "count", nullable = false)
    val count: Int = 0,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductDto = ProductDto(),

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    val order: CustomerOrderDto = CustomerOrderDto(),
) : BaseDto<OrderItem> {
    companion object {
        fun fromModel(model: OrderItem) = with(model) {
            OrderItemDto(
                id = id,
                count = count,
                product = ProductDto.fromModel(product),
                order = CustomerOrderDto.fromModel(order)
            )
        }
    }

    override fun toModel() = OrderItem(
        id = id,
        count = count,
        product = product.toModel(),
        order = order.toModel()
    )
}
