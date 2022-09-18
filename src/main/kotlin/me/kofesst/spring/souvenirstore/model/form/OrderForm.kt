package me.kofesst.spring.souvenirstore.model.form

import me.kofesst.spring.souvenirstore.model.Cart
import me.kofesst.spring.souvenirstore.model.CustomerOrder
import javax.validation.constraints.NotNull

data class OrderForm(
    @field:NotNull(message = "Это обязательное поле")
    var pointId: Long? = null,
) {
    fun toModel(cart: Cart) = CustomerOrder(
        customer = cart.customer,
        code = cart.code,
        items = cart.items.map { it.toOrderItem() }
    )
}