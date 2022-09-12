package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.UserDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : CrudRepository<UserDto, Long> {
    fun findByLogin(login: String): UserDto?
}