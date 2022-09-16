package me.kofesst.spring.souvenirstore.controller.hr

import me.kofesst.spring.souvenirstore.database.EmployeeDto
import me.kofesst.spring.souvenirstore.database.UserDto
import me.kofesst.spring.souvenirstore.model.UserRole
import me.kofesst.spring.souvenirstore.model.form.EmployeeForm
import me.kofesst.spring.souvenirstore.model.form.UserForm
import me.kofesst.spring.souvenirstore.repository.EmployeesRepository
import me.kofesst.spring.souvenirstore.repository.UsersRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/employees")
class EmployeesController @Autowired constructor(
    private val repository: EmployeesRepository,
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
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
//            employee.fullName.lowercase().contains(query.lowercase())
            true
        }
        model.addAttribute("models", employees)
        model.addAttribute("query", query)
        return "employees/overview"
    }

    @GetMapping("/add")
    fun add(
        employee: EmployeeForm,
        user: UserForm,
        model: Model,
    ): String {
        val positions = UserRole.positions()
        model.addAttribute("positions", positions)
        model.addAttribute("employee", employee)
        model.addAttribute("user", user)
        return "employees/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("employee") employeeForm: EmployeeForm,
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            val positions = UserRole.positions()
            model.addAttribute("positions", positions)
            return "employees/add"
        }

        val employee = employeeForm.toModel()
        val user = userForm.toModel().copy(
            password = passwordEncoder.encode(userForm.password),
            role = employeeForm.getRole()
        )

        if (usersRepository.findByLogin(user.login) != null) {
            result.rejectValue("login", "error.existing_login", "Данный логин уже используется")
            val positions = UserRole.positions()
            model.addAttribute("positions", positions)
            return "employees/add"
        }

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

        val positions = UserRole.positions()
        model.addAttribute("positions", positions)
        return "employees/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("employee") employeeForm: EmployeeForm,
        result: BindingResult,
        model: Model,
    ): String {
        if (result.hasErrors()) {
            val positions = UserRole.positions()
            model.addAttribute("id", id)
            model.addAttribute("positions", positions)
            return "employees/add"
        }

        val user = usersRepository.findByIdOrNull(employeeForm.userId)?.copy(
            role = employeeForm.getRole()
        )?.toModel() ?: return "redirect:/employees"
        val employee = employeeForm.toModel()

        employee.user = user
        repository.save(EmployeeDto.fromModel(employee))
        return "redirect:/employees"
    }
}