package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.CartDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CartsRepository : CrudRepository<CartDto, Long> {
    fun findByCustomerUserLogin(login: String): CartDto?
}