package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.PositionDto
import me.kofesst.spring.souvenirstore.model.form.PositionForm
import me.kofesst.spring.souvenirstore.repository.EmployeesRepository
import me.kofesst.spring.souvenirstore.repository.PositionsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/positions")
class PositionsController @Autowired constructor(
    private val repository: PositionsRepository,
    private val employeesRepository: EmployeesRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam(name = "referencedDelete", required = false) referenced: Boolean,
        model: Model,
    ): String {
        val positions = repository.findAll().asModels()
        model.addAttribute("models", positions)
        model.addAttribute("referenced", referenced)
        return "positions/overview"
    }

    @PostMapping
    fun search(
        @RequestParam(name = "query") query: String,
        model: Model,
    ): String {
        if (query.isBlank()) {
            return "redirect:/positions"
        }

        val positions = repository.findAll().asModels().filter { position ->
            position.title.lowercase().contains(query.lowercase())
        }
        model.addAttribute("models", positions)
        model.addAttribute("query", query)
        return "positions/overview"
    }

    @GetMapping("/add")
    fun add(position: PositionForm, model: Model): String {
        model.addAttribute("position", position)
        return "positions/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("position") position: PositionForm,
        result: BindingResult,
    ): String {
        if (result.hasErrors()) {
            return "positions/add"
        }

        repository.save(PositionDto.fromModel(position.toModel()))
        return "redirect:/positions"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val hasReference = employeesRepository.findAll().any { employee ->
            employee.position.id == id
        }

        if (hasReference) {
            return "redirect:/positions?referencedDelete=true"
        }

        repository.deleteById(id)
        return "redirect:/positions"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val position = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/positions"
        model.addAttribute("id", id)
        model.addAttribute("position", PositionForm.fromModel(position))
        return "positions/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("position") position: PositionForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("id", id)
            return "positions/add"
        }

        repository.save(PositionDto.fromModel(position.toModel()))
        return "redirect:/positions"
    }
}