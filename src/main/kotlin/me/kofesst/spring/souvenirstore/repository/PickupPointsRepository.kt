package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.PickupPointDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PickupPointsRepository : CrudRepository<PickupPointDto, Long> {
    fun findByAddressIgnoreCase(address: String): PickupPointDto?
}