package me.kofesst.spring.souvenirstore.controller.customer

import me.kofesst.spring.souvenirstore.database.CartDto
import me.kofesst.spring.souvenirstore.database.CartItemDto
import me.kofesst.spring.souvenirstore.repository.CartItemsRepository
import me.kofesst.spring.souvenirstore.repository.CartsRepository
import me.kofesst.spring.souvenirstore.repository.CustomersRepository
import me.kofesst.spring.souvenirstore.repository.PromoCodesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.security.Principal
import kotlin.math.min

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAnyAuthority('User')")
class UserCartController @Autowired constructor(
    private val repository: CartsRepository,
    private val itemsRepository: CartItemsRepository,
    private val customersRepository: CustomersRepository,
    private val promoCodesRepository: PromoCodesRepository,
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
        val item = itemsRepository.findByIdOrNull(itemId)?.toModel() ?: return "redirect:/cart"
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        item.count = min(item.count + 1, Int.MAX_VALUE)

        itemsRepository.save(
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
        val item = itemsRepository.findByIdOrNull(itemId)?.toModel() ?: return "redirect:/cart"
        val cart = repository.findByCustomerUserLogin(user.name)?.toModel() ?: return "redirect:/cart"
        item.count -= 1

        if (item.count <= 0) {
            itemsRepository.deleteById(itemId)
        } else {
            itemsRepository.save(
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
        itemsRepository.deleteById(itemId)
        return "redirect:/cart"
    }
}