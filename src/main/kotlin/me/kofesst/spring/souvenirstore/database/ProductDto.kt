package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.Product
import javax.persistence.*

@Entity
@Table(name = "product")
data class ProductDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "title", unique = true, nullable = false, length = 30)
    val title: String = "",

    @Column(name = "description", nullable = false, length = 255)
    val description: String = "",

    @Column(name = "price", nullable = false, precision = 2)
    val price: Double = 0.0,

    @Column(name = "old_price", precision = 2)
    val oldPrice: Double = 0.0,

    @Column(name = "stock_amount", nullable = false)
    val stockAmount: Int = 0,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "category_id", nullable = false)
    val category: ProductCategoryDto = ProductCategoryDto(),
) : BaseDto<Product> {
    companion object {
        fun fromModel(model: Product) = with(model) {
            ProductDto(
                id = id,
                title = title,
                description = description,
                price = price,
                oldPrice = oldPrice,
                stockAmount = stockAmount,
                category = ProductCategoryDto.fromModel(category)
            )
        }
    }

    override fun toModel() = Product(
        id = id,
        title = title,
        description = description,
        price = price,
        oldPrice = oldPrice,
        stockAmount = stockAmount,
        category = category.toModel()
    )
}
