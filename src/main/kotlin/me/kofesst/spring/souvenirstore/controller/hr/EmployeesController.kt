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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/hr/employees")
@PreAuthorize("hasAnyAuthority('HR')")
class EmployeesController @Autowired constructor(
    private val repository: EmployeesRepository,
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val employees = repository.findAll().asModels().run {
            if (query?.isNotBlank() == true) {
                model.addAttribute("query", query)
                filter { employee ->
                    employee.user.fullName.lowercase().contains(query.lowercase())
                }
            } else {
                this
            }
        }
        model.addAttribute("models", employees)
        return "pages/hr/employees/overview"
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
        model.addAttribute(
            "user",
            user.copy(
                login = null,
                password = null
            )
        )
        return "pages/hr/employees/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("employee") employeeForm: EmployeeForm,
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
        model: Model,
    ): String {
        val positions = UserRole.positions()
        model.addAttribute("positions", positions)

        if (result.hasErrors()) {
            return "pages/hr/employees/add"
        }

        val employee = employeeForm.toModel()
        val user = userForm.toModel(passwordEncoder)
        if (usersRepository.findByLogin(user.login) != null) {
            result.rejectValue("login", "error.existing_login", "Данный логин уже используется")
            return "pages/hr/employees/add"
        }

        employee.user = usersRepository.save(UserDto.fromModel(user)).toModel()
        repository.save(EmployeeDto.fromModel(employee))
        return "redirect:/hr/employees"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val employee = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/hr/employees"
        model.addAttribute("id", id)
        model.addAttribute("employee", EmployeeForm.fromModel(employee))
        model.addAttribute("user", UserForm.fromModel(employee.user))

        val positions = UserRole.positions()
        model.addAttribute("positions", positions)
        return "pages/hr/employees/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("employee") employeeForm: EmployeeForm,
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
        model: Model,
    ): String {
        val positions = UserRole.positions()
        model.addAttribute("positions", positions)
        model.addAttribute("id", id)

        if (result.hasErrors()) {
            return "pages/hr/employees/add"
        }

        val employee = employeeForm.toModel()
        val existing = usersRepository.findByIdOrNull(userForm.id) ?: return "redirect:/hr/employees"
        val user = userForm.toModel(existing.toModel())
        employee.user = user
        repository.save(EmployeeDto.fromModel(employee))
        return "redirect:/hr/employees"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): String {
        val employee = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/hr/employees"
        repository.deleteById(id)
        usersRepository.deleteById(employee.user.id)
        return "redirect:/hr/employees"
    }
}