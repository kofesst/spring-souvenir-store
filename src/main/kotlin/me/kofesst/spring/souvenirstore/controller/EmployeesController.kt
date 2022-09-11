package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.EmployeeDto
import me.kofesst.spring.souvenirstore.database.UserDto
import me.kofesst.spring.souvenirstore.model.form.EmployeeForm
import me.kofesst.spring.souvenirstore.repository.EmployeesRepository
import me.kofesst.spring.souvenirstore.repository.PositionsRepository
import me.kofesst.spring.souvenirstore.repository.UsersRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
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
    private val usersRepository: UsersRepository,
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
    fun add(employee: EmployeeForm, model: Model): String {
        val positions = positionsRepository.findAll().asModels()
        model.addAttribute("positions", positions)
        model.addAttribute("employee", employee)
        return "employees/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("employee") form: EmployeeForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            val positions = positionsRepository.findAll().asModels()
            model.addAttribute("positions", positions)
            return "employees/add"
        }

        val positions = positionsRepository.findAll().asModels()
        val employee = form.toModel(positions)
        val user = employee.user

        employee.user = usersRepository.save(UserDto.fromModel(user)).toModel()
        repository.save(EmployeeDto.fromModel(employee))
        return "redirect:/employees"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): String {
        val employee = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/employees"
        repository.deleteById(id)
        usersRepository.deleteById(employee.user.id)
        return "redirect:/employees"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val employee = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/employees"
        model.addAttribute("id", id)
        model.addAttribute("employee", EmployeeForm.fromModel(employee))

        val positions = positionsRepository.findAll().asModels()
        model.addAttribute("positions", positions)
        return "employees/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("employee") employee: EmployeeForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            val positions = positionsRepository.findAll().asModels()
            model.addAttribute("id", id)
            model.addAttribute("positions", positions)
            return "employees/add"
        }

        val positions = positionsRepository.findAll().asModels()
        repository.save(EmployeeDto.fromModel(employee.toModel(positions)))
        return "redirect:/employees"
    }
}