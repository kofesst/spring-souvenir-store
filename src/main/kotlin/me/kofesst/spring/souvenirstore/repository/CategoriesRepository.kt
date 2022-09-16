package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.ProductCategoryDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriesRepository : CrudRepository<ProductCategoryDto, Long> {
    fun findByDisplayNameIgnoreCase(displayName: String): ProductCategoryDto?
}