package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.CustomerOrderDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepository : CrudRepository<CustomerOrderDto, Long> {
    fun findByCustomerUserLogin(login: String): List<CustomerOrderDto>
}