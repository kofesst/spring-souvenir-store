package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.CustomerDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomersRepository : CrudRepository<CustomerDto, Long>