package com.moneyapi.moneyapi.service

import com.moneyapi.moneyapi.db.entity.User
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
@Repository
interface UserService {
    fun findByEmail(email: String): User
    fun createOrUpdate(user: User): User
}