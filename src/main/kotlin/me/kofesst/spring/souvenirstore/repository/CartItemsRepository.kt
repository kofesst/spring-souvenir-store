package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.CartItemDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemsRepository : CrudRepository<CartItemDto, Long>