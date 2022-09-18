package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Cart
import javax.persistence.*

@Entity
@Table(name = "cart")
data class CartDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart", unique = true, nullable = false)
    val id: Long = 0,

    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "customer_id")
    val customer: CustomerDto = CustomerDto(),

    @OneToMany(mappedBy = "cart")
    val items: List<CartItemDto> = emptyList(),

    @ManyToOne
    @JoinColumn(name = "code_id")
    val code: PromoCodeDto? = null,
) : BaseDto<Cart> {
    companion object {
        fun fromModel(model: Cart) = with(model) {
            CartDto(
                id = id,
                customer = CustomerDto.fromModel(customer),
                items = items.map { CartItemDto.fromModel(it) },
                code = if (code != null) {
                    PromoCodeDto.fromModel(code!!)
                } else {
                    null
                }
            )
        }
    }

    override fun toModel() = Cart(
        id = id,
        customer = customer.toModel(),
        items = items.map { it.toModel() }.toMutableList(),
        code = code?.toModel()
    )
}
