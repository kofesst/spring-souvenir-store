package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.PositionDto
import me.kofesst.spring.souvenirstore.model.form.PositionForm
import me.kofesst.spring.souvenirstore.repository.PositionsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/positions")
class PositionsController @Autowired constructor(
    private val repository: PositionsRepository,
) {
    @GetMapping
    fun overview(model: Model): String {
        val positions = repository.findAll().asModels()
        model.addAttribute("models", positions)
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
}