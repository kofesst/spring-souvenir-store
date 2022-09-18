package me.kofesst.spring.souvenirstore.controller.customer

import me.kofesst.spring.souvenirstore.database.CartDto
import me.kofesst.spring.souvenirstore.database.CartItemDto
import me.kofesst.spring.souvenirstore.database.CustomerOrderDto
import me.kofesst.spring.souvenirstore.database.OrderItemDto
import me.kofesst.spring.souvenirstore.model.form.OrderForm
import me.kofesst.spring.souvenirstore.repository.*
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid
import kotlin.math.min

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAnyAuthority('User')")
class UserCartController @Autowired constructor(
    private val repository: CartsRepository,
    private val cartItemsRepository: CartItemsRepository,
    private val customersRepository: CustomersRepository,
    private val promoCodesRepository: PromoCodesRepository,
    private val pickupPointsRepository: PickupPointsRepository,
    private val ordersRepository: OrdersRepository,
    private val orderItemsRepository: OrderItemsRepository,
) {
    @GetMapping
    fun overview(
        user: Principal,
        model: Model,
    ): String {
        val customer = customersRepository.findByUserLogin(user.name) ?: return "redirect:/login"
        val cart = with(repository.findByCustomerUserLogin(user.name)) {
            this ?: repository.save(CartDto(customer = customer))
        }.toModel()

        model.addAttribute("cart", cart)
        return "pages/customers/cart/overview"
    }

    @GetMapping("/order")
    fun order(
        orderForm: OrderForm,
        user: Principal,
        model: Model,
    ): String {
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        if (cart.size == 0) {
            return "redirect:/cart"
        }

        model.addAttribute("cart", cart)
        model.addAttribute("order", orderForm)

        val pickupPoints = pickupPointsRepository.findAll().asModels()
        model.addAttribute("points", pickupPoints)
        return "pages/customers/cart/order"
    }

    @PostMapping("/order")
    fun order(
        @Valid @ModelAttribute("order") orderForm: OrderForm,
        result: BindingResult,
        user: Principal,
        model: Model,
    ): String {
        val pickupPoints = pickupPointsRepository.findAll().asModels()
        model.addAttribute("points", pickupPoints)

        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        model.addAttribute("cart", cart)

        if (result.hasErrors()) {
            return "pages/customers/cart/order"
        }

        val pickupPoint = pickupPointsRepository.findByIdOrNull(orderForm.pointId!!)
        if (pickupPoint == null) {
            result.rejectValue("pointId", "error.required", "Это обязательное поле")
            return "pages/customers/cart/order"
        }

        val order = orderForm.toModel(cart).copy(
            point = pickupPoint.toModel()
        )
        val savedOrder = ordersRepository.save(CustomerOrderDto.fromModel(order))
        orderItemsRepository.saveAll(
            order.items.map {
                OrderItemDto.fromModel(it).copy(
                    order = savedOrder
                )
            }
        )

        cart.code = null
        repository.save(CartDto.fromModel(cart))
        cartItemsRepository.deleteByCartId(cart.id)
        return "redirect:/orders"
    }

    @PostMapping("/apply-code")
    fun applyCode(
        @RequestParam("code") code: String,
        user: Principal,
    ): String {
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        val promoCode = promoCodesRepository.findByCode(code)?.toModel()
        if (promoCode != null && promoCode.active) {
            cart.code = promoCode
            repository.save(CartDto.fromModel(cart))
        }

        return "redirect:/cart"
    }

    @PostMapping("/remove-code")
    fun removeCode(
        user: Principal,
        model: Model,
    ): String {
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        cart.code = null
        repository.save(CartDto.fromModel(cart))
        return "redirect:/cart"
    }

    @PostMapping("/plus/{id}")
    fun plus(
        @PathVariable("id") itemId: Long,
        user: Principal,
    ): String {
        val item = cartItemsRepository.findByIdOrNull(itemId)?.toModel() ?: return "redirect:/cart"
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        item.count = min(item.count + 1, Int.MAX_VALUE)

        cartItemsRepository.save(
            CartItemDto.fromModel(item).copy(
                cart = CartDto.fromModel(cart)
            )
        )

        return "redirect:/cart"
    }

    @PostMapping("/minus/{id}")
    fun minus(
        @PathVariable("id") itemId: Long,
        user: Principal,
    ): String {
        val item = cartItemsRepository.findByIdOrNull(itemId)?.toModel() ?: return "redirect:/cart"
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        item.count -= 1

        if (item.count <= 0) {
            cartItemsRepository.deleteById(itemId)
        } else {
            cartItemsRepository.save(
                CartItemDto.fromModel(item).copy(
                    cart = CartDto.fromModel(cart)
                )
            )
        }

        return "redirect:/cart"
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") itemId: Long,
        user: Principal,
    ): String {
        cartItemsRepository.deleteById(itemId)
        return "redirect:/cart"
    }
}