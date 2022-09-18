package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.CartItemDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CartItemsRepository : CrudRepository<CartItemDto, Long> {
    @Transactional
    fun deleteByCartId(id: Long): Long
}