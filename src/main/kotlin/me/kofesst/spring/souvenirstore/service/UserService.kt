package me.kofesst.spring.souvenirstore.service

import me.kofesst.spring.souvenirstore.repository.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val usersRepository: UsersRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null) return null

        val dto = usersRepository.findByLogin(username)
        return dto?.toModel()
    }
}