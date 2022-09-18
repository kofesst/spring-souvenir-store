package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.PromoCodeDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PromoCodesRepository : CrudRepository<PromoCodeDto, Long> {
    fun findByCodeIgnoreCase(code: String): PromoCodeDto?
    fun findByCode(code: String): PromoCodeDto?
}