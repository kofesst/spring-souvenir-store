package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.OrderItemDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemsRepository : CrudRepository<OrderItemDto, Long>