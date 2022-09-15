package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Cart
import javax.persistence.*

@Entity
@Table(name = "cart")
data class CartDto(
    @Id
    @Column(name = "ROWID")
    val rowId: String = "",

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "customer_id")
    val customer: CustomerDto = CustomerDto(),

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "product_id")
    val product: ProductDto = ProductDto(),

    @Column(name = "count", nullable = false)
    val count: Int = 0,
) : BaseDto<Cart> {
    companion object {
        fun fromModel(model: Cart) = with(model) {
            CartDto(
                rowId = rowId,
                customer = CustomerDto.fromModel(customer),
                product = ProductDto.fromModel(product),
                count = count
            )
        }
    }

    override fun toModel() = Cart(
        rowId = rowId,
        customer = customer.toModel(),
        product = product.toModel(),
        count = count
    )
}
