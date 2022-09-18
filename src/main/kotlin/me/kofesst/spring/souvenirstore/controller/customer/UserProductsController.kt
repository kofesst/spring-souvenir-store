package me.kofesst.spring.souvenirstore.controller.customer

import me.kofesst.spring.souvenirstore.repository.CategoriesRepository
import me.kofesst.spring.souvenirstore.repository.ProductsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/products")
@PreAuthorize("hasAnyAuthority('User')")
class UserProductsController @Autowired constructor(
    private val repository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
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
}