package com.moneyapi.moneyapi.service

import com.moneyapi.moneyapi.db.entity.User
import com.moneyapi.moneyapi.db.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
@Repository
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun findByEmail(email: String) = userRepository.findByemail(email)

    override fun createOrUpdate(user: User) = userRepository.save(user)
}