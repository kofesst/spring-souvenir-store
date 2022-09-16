package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.ProductDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ProductsRepository : CrudRepository<ProductDto, Long> {
    fun findByTitleIgnoreCase(title: String): ProductDto?

    fun countByCategoryId(id: Long): Long

    @Transactional
    fun deleteByCategoryId(id: Long): Long
}