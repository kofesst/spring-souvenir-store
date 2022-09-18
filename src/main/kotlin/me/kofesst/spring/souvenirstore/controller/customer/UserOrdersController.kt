package me.kofesst.spring.souvenirstore.controller.customer

import me.kofesst.spring.souvenirstore.repository.OrdersRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/orders")
@PreAuthorize("hasAnyAuthority('User')")
class UserOrdersController @Autowired constructor(
    private val repository: OrdersRepository,
) {
    @GetMapping
    fun overview(
        user: Principal,
        model: Model,
    ): String {
        val orders = repository.findByCustomerUserLogin(user.name).asModels()
        model.addAttribute("models", orders)
        return "pages/customers/orders/overview"
    }
}