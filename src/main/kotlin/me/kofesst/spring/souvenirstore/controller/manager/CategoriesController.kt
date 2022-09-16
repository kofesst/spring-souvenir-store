package me.kofesst.spring.souvenirstore.controller.manager

import me.kofesst.spring.souvenirstore.database.ProductCategoryDto
import me.kofesst.spring.souvenirstore.model.ProductCategory
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
            category to productsRepository.countByCategoryId(category.id)
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
        val category = checkCategory(
            categoryForm = categoryForm,
            result = result
        ) ?: return "pages/manager/categories/add"

        repository.save(ProductCategoryDto.fromModel(category))
        return "redirect:/manager/categories"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val category = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/categories"
        model.addAttribute("id", id)
        model.addAttribute("category", ProductCategoryForm.fromModel(category))
        return "pages/manager/categories/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("category") categoryForm: ProductCategoryForm,
        result: BindingResult,
        model: Model,
    ): String {
        model.addAttribute("id", id)

        val category = checkCategory(
            editing = true,
            categoryForm = categoryForm,
            result = result
        ) ?: return "pages/manager/categories/add"

        repository.save(ProductCategoryDto.fromModel(category))
        return "redirect:/manager/categories"
    }

    private fun checkCategory(
        editing: Boolean = false,
        categoryForm: ProductCategoryForm,
        result: BindingResult,
    ): ProductCategory? {
        if (result.hasErrors()) {
            return null
        }

        return checkForCategoryName(
            compareIds = editing,
            categoryForm = categoryForm,
            result = result
        )
    }

    private fun checkForCategoryName(
        compareIds: Boolean = false,
        categoryForm: ProductCategoryForm,
        result: BindingResult,
    ): ProductCategory? {
        val category = categoryForm.toModel()
        if (
            with(repository.findByDisplayNameIgnoreCase(category.displayName)) {
                this != null && if (compareIds) {
                    this.id != category.id
                } else {
                    true
                }
            }
        ) {
            result.rejectValue("displayName", "error.existing_title", "Это название уже занято")
            return null
        }

        return category
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        repository.deleteById(id)
        return "redirect:/manager/categories"
    }
}