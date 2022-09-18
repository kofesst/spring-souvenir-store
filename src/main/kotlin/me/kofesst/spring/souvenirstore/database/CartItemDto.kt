package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.CartItem
import javax.persistence.*

@Entity
@Table(name = "cart_item")
data class CartItemDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item", unique = true, nullable = false)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductDto = ProductDto(),

    @Column(name = "count", nullable = false)
    val count: Int = 0,

    @ManyToOne(cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "cart_id", nullable = false)
    val cart: CartDto = CartDto(),
) : BaseDto<CartItem> {
    companion object {
        fun fromModel(model: CartItem) = with(model) {
            CartItemDto(
                id = id,
                product = ProductDto.fromModel(product),
                count = count,
            )
        }
    }

    override fun toModel() = CartItem(
        id = id,
        product = product.toModel(),
        count = count,
    )
}