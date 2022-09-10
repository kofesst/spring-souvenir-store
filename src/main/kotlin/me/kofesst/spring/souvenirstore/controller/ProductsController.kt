package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.ProductDto
import me.kofesst.spring.souvenirstore.model.form.ProductForm
import me.kofesst.spring.souvenirstore.repository.ProductsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/products")
class ProductsController @Autowired constructor(
    private val repository: ProductsRepository,
) {
    @GetMapping
    fun overview(model: Model): String {
        val products = repository.findAll().asModels()
        model.addAttribute("models", products)
        return "products/overview"
    }

    @PostMapping
    fun search(
        @RequestParam(name = "query") query: String,
        model: Model,
    ): String {
        if (query.isBlank()) {
            return "redirect:/products"
        }

        val products = repository.findAll().asModels().filter { product ->
            product.title.lowercase().contains(query.lowercase()) ||
                    product.description.lowercase().contains(query.lowercase())
        }
        model.addAttribute("models", products)
        model.addAttribute("query", query)
        return "products/overview"
    }

    @GetMapping("/add")
    fun add(product: ProductForm, model: Model): String {
        model.addAttribute("product", product)
        return "products/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("product") product: ProductForm,
        result: BindingResult,
    ): String {
        if (result.hasErrors()) {
            return "products/add"
        }

        repository.save(ProductDto.fromModel(product.toModel()))
        return "redirect:/products"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        repository.deleteById(id)
        return "redirect:/products"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val product = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/products"
        model.addAttribute("id", id)
        model.addAttribute("product", ProductForm.fromModel(product))
        return "products/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("product") product: ProductForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("id", id)
            return "products/add"
        }

        repository.save(ProductDto.fromModel(product.toModel()))
        return "redirect:/products"
    }
}