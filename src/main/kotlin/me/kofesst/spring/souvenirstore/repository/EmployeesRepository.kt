package me.kofesst.spring.souvenirstore.repository

import me.kofesst.spring.souvenirstore.database.EmployeeDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeesRepository : CrudRepository<EmployeeDto, Long>