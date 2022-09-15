package me.kofesst.spring.souvenirstore.database

import me.kofesst.spring.souvenirstore.model.ProductCategory
import javax.persistence.*

@Entity
@Table(name = "product_category")
data class ProductCategoryDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "display_name", unique = true, nullable = false, length = 30)
    val displayName: String = "",

    @OneToMany(mappedBy = "category")
    val products: List<ProductDto> = emptyList(),
) : BaseDto<ProductCategory> {
    companion object {
        fun fromModel(model: ProductCategory) = with(model) {
            ProductCategoryDto(
                id = id,
                displayName = displayName,
                products = products.map { ProductDto.fromModel(it) }
            )
        }
    }

    override fun toModel() = ProductCategory(
        id = id,
        displayName = displayName,
        products = products.map { it.toModel() }
    )
}