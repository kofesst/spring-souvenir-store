package me.kofesst.spring.souvenirstore.controller.manager

import me.kofesst.spring.souvenirstore.database.ProductCategoryDto
import me.kofesst.spring.souvenirstore.model.form.ProductCategoryForm
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
@RequestMapping("/manager/categories")
@PreAuthorize("hasAnyAuthority('Manager')")
class CategoriesController @Autowired constructor(
    private val repository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val categoriesWithProductsCount = repository.findAll().asModels().run {
            if (query?.isNotBlank() == true) {
                model.addAttribute("query", query)
                filter { category ->
                    category.displayName.lowercase().contains(query.lowercase())
                }
            } else {
                this
            }
        }.map { category ->
            category to productsRepository.countByCategory_Id(category.id)
        }
        model.addAttribute("models", categoriesWithProductsCount)
        return "pages/manager/categories/overview"
    }

    @GetMapping("/add")
    fun add(category: ProductCategoryForm, model: Model): String {
        model.addAttribute("category", category)
        return "pages/manager/categories/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("category") categoryForm: ProductCategoryForm,
        result: BindingResult,
    ): String {
        if (result.hasErrors()) {
            return "pages/manager/categories/add"
        }

        val category = categoryForm.toModel()
        if (repository.findByDisplayNameIgnoreCase(category.displayName) != null) {
            result.rejectValue("displayName", "error.existing_title", "Это название уже занято")
            return "pages/manager/categories/add"
        }

        repository.save(ProductCategoryDto.fromModel(category))
        return "redirect:/manager/categories"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        repository.deleteById(id)
        return "redirect:/manager/categories"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val category = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/categories"
        model.addAttribute("id", id)
        model.addAttribute("product", ProductCategoryForm.fromModel(category))
        return "pages/manager/categories/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("category") categoryForm: ProductCategoryForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("id", id)
            return "pages/manager/categories/add"
        }

        val category = categoryForm.toModel()
        if (repository.findByDisplayNameIgnoreCase(category.displayName) != null) {
            result.rejectValue("displayName", "error.existing_title", "Это название уже занято")
            return "pages/manager/categories/add"
        }

        repository.save(ProductCategoryDto.fromModel(category))
        return "redirect:/manager/categories"
    }
}