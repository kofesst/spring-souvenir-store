package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.PositionDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PositionsRepository : CrudRepository<PositionDto, Long>