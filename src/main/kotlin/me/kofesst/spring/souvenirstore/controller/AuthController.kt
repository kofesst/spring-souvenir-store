package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.CustomerDto
import me.kofesst.spring.souvenirstore.database.UserDto
import me.kofesst.spring.souvenirstore.model.Customer
import me.kofesst.spring.souvenirstore.model.UserRole
import me.kofesst.spring.souvenirstore.model.form.UserForm
import me.kofesst.spring.souvenirstore.repository.CustomersRepository
import me.kofesst.spring.souvenirstore.repository.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class AuthController @Autowired constructor(
    private val repository: UsersRepository,
    private val customersRepository: CustomersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @GetMapping("/registration")
    fun registration(
        userForm: UserForm,
        model: Model,
    ): String {
        model.addAttribute(
            "user",
            userForm.copy(
                login = null,
                password = null,
                role = UserRole.User.authority
            )
        )
        return "auth/registration"
    }

    @PostMapping("/registration")
    fun registration(
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
    ): String {
        if (result.hasErrors()) {
            println(result.allErrors)
            return "auth/registration"
        }

        val user = userForm.toModel(passwordEncoder).let { user ->
            user.copy(
                password = passwordEncoder.encode(user.password)
            )
        }

        if (repository.findByLogin(user.login) != null) {
            result.rejectValue("login", "error.existing_login", "Данный логин уже используется")
            return "auth/registration"
        }

        val customer = Customer(
            user = repository.save(UserDto.fromModel(user)).toModel()
        )
        customersRepository.save(CustomerDto.fromModel(customer))
        return "redirect:/login"
    }

    @GetMapping("/home")
    fun homeRedirect(): String {
        val roles = SecurityContextHolder.getContext().authentication.authorities
        val role = roles.map { UserRole.valueOf(it.authority) }.firstOrNull() ?: return "redirect:/error"
        println(role)
        return when (role) {
            UserRole.User -> "redirect:/products"
            UserRole.HR -> "redirect:/hr/employees"
            UserRole.Cashier -> "redirect:/cashier"
            UserRole.Accountant -> "redirect:/accountant"
            UserRole.Director -> "redirect:/employees"
            UserRole.StockMan -> TODO()
            UserRole.Manager -> "redirect:/manager/categories"
        }
    }
}