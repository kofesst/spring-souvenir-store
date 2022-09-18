package me.kofesst.spring.souvenirstore.controller.customer

import me.kofesst.spring.souvenirstore.database.CartDto
import me.kofesst.spring.souvenirstore.database.CartItemDto
import me.kofesst.spring.souvenirstore.repository.CartItemsRepository
import me.kofesst.spring.souvenirstore.repository.CartsRepository
import me.kofesst.spring.souvenirstore.repository.CategoriesRepository
import me.kofesst.spring.souvenirstore.repository.ProductsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Controller
@RequestMapping("/products")
@PreAuthorize("hasAnyAuthority('User')")
class UserProductsController @Autowired constructor(
    private val repository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val cartsRepository: CartsRepository,
    private val cartItemsRepository: CartItemsRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        @RequestParam("categoryId") categoryId: Long?,
        @RequestParam("minimumPrice") minimumPrice: Double?,
        @RequestParam("maximumPrice") maximumPrice: Double?,
        model: Model,
    ): String {
        model.addAttribute("query", query)
        model.addAttribute("categoryId", categoryId)
        model.addAttribute("minimumPrice", minimumPrice)
        model.addAttribute("maximumPrice", maximumPrice)

        val categories = categoriesRepository.findAll().asModels()
        model.addAttribute("categories", categories)

        val products = repository.findByPriceGreaterThanEqualAndPriceLessThanEqual(
            minimumPrice = minimumPrice ?: Double.MIN_VALUE,
            maximumPrice = maximumPrice ?: Double.MAX_VALUE
        ).asModels().run {
            (if (query?.isNotBlank() == true) {
                model.addAttribute("query", query)
                filter { product ->
                    product.title.lowercase().contains(
                        other = query.lowercase()
                    ) || product.description.lowercase().contains(
                        other = query.lowercase()
                    )
                }
            } else {
                this
            }).filter { product ->
                if (categoryId != null) {
                    product.category.id == categoryId
                } else {
                    true
                }
            }
        }
        model.addAttribute("models", products)
        return "pages/customers/products/overview"
    }

    @PostMapping("/add-to-cart/{id}")
    fun addToCart(
        @PathVariable("id") productId: Long,
        user: Principal,
        model: Model,
    ): String {
        val product = repository.findByIdOrNull(productId)?.toModel() ?: return "redirect:/products"
        val cart = cartsRepository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/products"
        cartItemsRepository.save(
            CartItemDto.fromModel(
                cart.addProduct(product)
            ).copy(
                cart = CartDto.fromModel(cart)
            )
        )
        return "redirect:/products"
    }
}