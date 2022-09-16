package me.kofesst.spring.souvenirstore.controller.manager

import me.kofesst.spring.souvenirstore.database.ProductDto
import me.kofesst.spring.souvenirstore.model.form.ProductForm
import me.kofesst.spring.souvenirstore.repository.CategoriesRepository
import me.kofesst.spring.souvenirstore.repository.ProductsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/manager/products")
@PreAuthorize("hasAnyAuthority('Manager')")
class ProductsController @Autowired constructor(
    private val repository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val products = repository.findAll().asModels().run {
            if (query?.isNotBlank() == true) {
                model.addAttribute("query", query)
                filter { product ->
                    product.title.lowercase().contains(query.lowercase()) ||
                            product.description.lowercase().contains(query.lowercase())
                }
            } else {
                this
            }
        }
        model.addAttribute("models", products)
        return "pages/manager/products/overview"
    }

    @GetMapping("/add")
    fun add(product: ProductForm, model: Model): String {
        val categories = categoriesRepository.findAll().asModels()
        model.addAttribute("categories", categories)
        model.addAttribute("product", product)
        return "pages/manager/products/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("product") productForm: ProductForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            return "pages/manager/products/add"
        }

        val product = productForm.toModel()
        if (repository.findByTitleIgnoreCase(product.title) != null) {
            val categories = categoriesRepository.findAll().asModels()
            model.addAttribute("categories", categories)
            result.rejectValue("title", "error.existing_title", "Это название уже занято")
            return "pages/manager/products/add"
        }

        val category = categoriesRepository.findByIdOrNull(productForm.categoryId!!)
        if (category == null) {
            val categories = categoriesRepository.findAll().asModels()
            model.addAttribute("categories", categories)
            result.rejectValue("categoryId", "error.required", "Это обязательное поле")
            return "pages/manager/products/add"
        }

        product.category = category.toModel()
        repository.save(ProductDto.fromModel(product))
        return "redirect:/manager/products"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        repository.deleteById(id)
        return "redirect:/manager/products"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val categories = categoriesRepository.findAll().asModels()
        model.addAttribute("categories", categories)

        val product = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/products"
        model.addAttribute("id", id)
        model.addAttribute("product", ProductForm.fromModel(product))
        return "pages/manager/products/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("product") productForm: ProductForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("id", id)
            return "pages/manager/products/add"
        }

        val product = productForm.toModel()
        if (
            with(repository.findByTitleIgnoreCase(product.title)) {
                this != null && this.id != product.id
            }
        ) {
            val categories = categoriesRepository.findAll().asModels()
            model.addAttribute("categories", categories)
            result.rejectValue("title", "error.existing_title", "Это название уже занято")
            return "pages/manager/products/add"
        }

        val category = categoriesRepository.findByIdOrNull(productForm.categoryId!!)
        if (category == null) {
            val categories = categoriesRepository.findAll().asModels()
            model.addAttribute("categories", categories)
            result.rejectValue("categoryId", "error.required", "Это обязательное поле")
            return "pages/manager/products/add"
        }

        product.category = category.toModel()
        repository.save(ProductDto.fromModel(product))
        return "redirect:/manager/products"
    }
}