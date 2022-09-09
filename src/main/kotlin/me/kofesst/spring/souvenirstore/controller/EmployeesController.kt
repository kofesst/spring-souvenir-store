package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.EmployeeDto
import me.kofesst.spring.souvenirstore.model.form.EmployeeForm
import me.kofesst.spring.souvenirstore.repository.EmployeesRepository
import me.kofesst.spring.souvenirstore.repository.PositionsRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/employees")
class EmployeesController @Autowired constructor(
    private val repository: EmployeesRepository,
    private val positionsRepository: PositionsRepository,
) {
    @GetMapping
    fun overview(model: Model): String {
        val employees = repository.findAll().asModels()
        model.addAttribute("models", employees)
        return "employees/overview"
    }

    @PostMapping
    fun search(
        @RequestParam(name = "query") query: String,
        model: Model,
    ): String {
        if (query.isBlank()) {
            return "redirect:/employees"
        }

        val employees = repository.findAll().asModels().filter { employee ->
            employee.fullName.lowercase().contains(query.lowercase())
        }
        model.addAttribute("models", employees)
        model.addAttribute("query", query)
        return "employees/overview"
    }

    @GetMapping("/add")
    fun add(position: EmployeeForm, model: Model): String {
        val positions = positionsRepository.findAll().asModels()
        model.addAttribute("positions", positions)
        model.addAttribute("employee", position)
        return "employees/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("employee") position: EmployeeForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            val positions = positionsRepository.findAll().asModels()
            model.addAttribute("positions", positions)
            return "employees/add"
        }

        val positions = positionsRepository.findAll().asModels()
        repository.save(EmployeeDto.fromModel(position.toModel(positions)))
        return "redirect:/employees"
    }
}