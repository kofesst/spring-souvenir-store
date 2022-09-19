package me.kofesst.spring.souvenirstore.controller.stockman

import me.kofesst.spring.souvenirstore.database.CustomerOrderDto
import me.kofesst.spring.souvenirstore.model.OrderStatus
import me.kofesst.spring.souvenirstore.repository.OrdersRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/stockman")
class StockManOrdersController(
    private val repository: OrdersRepository,
) {
    @GetMapping
    fun overview(
        model: Model,
    ): String {
        val orders = repository.findAll().asModels().filter { order ->
            order.status == OrderStatus.Preparing
        }
        model.addAttribute("models", orders)
        return "pages/stockman/overview"
    }

    @PostMapping("/send/{id}")
    fun send(
        @PathVariable("id") orderId: Long,
    ): String {
        val order = repository.findByIdOrNull(orderId)?.toModel() ?: return "redirect:/stockman"
        order.status = OrderStatus.OnTheWay
        repository.save(CustomerOrderDto.fromModel(order))
        return "redirect:/stockman"
    }
}