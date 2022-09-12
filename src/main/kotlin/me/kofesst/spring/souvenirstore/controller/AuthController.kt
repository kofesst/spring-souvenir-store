package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.database.UserDto
import me.kofesst.spring.souvenirstore.model.UserRole
import me.kofesst.spring.souvenirstore.model.form.UserForm
import me.kofesst.spring.souvenirstore.repository.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class AuthController @Autowired constructor(
    private val repository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @GetMapping("/registration")
    fun registration(form: UserForm): String {
        return "auth/registration"
    }

    @PostMapping("/registration")
    fun registration(
        @Valid @ModelAttribute("user") form: UserForm,
        result: BindingResult,
    ): String {
        val user = form.toModel().let { user ->
            user.copy(
                password = passwordEncoder.encode(user.password)
            )
        }
        repository.save(UserDto.fromModel(user))
        return "redirect:/login"
    }

    @GetMapping("/home")
    fun homeRedirect(): String {
        val roles = SecurityContextHolder.getContext().authentication.authorities
        println(roles)
        val role = roles.map { UserRole.valueOf(it.authority) }.firstOrNull() ?: return "redirect:/error"
        return when (role) {
            UserRole.User -> "redirect:/user"
            UserRole.HR -> "redirect:/hr"
            UserRole.Cashier -> "redirect:/cashier"
            UserRole.Accountant -> "redirect:/accountant"
            UserRole.Director -> "redirect:/employees"
        }
    }
}