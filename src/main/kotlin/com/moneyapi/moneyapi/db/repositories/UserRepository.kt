package com.moneyapi.moneyapi.db.repositories

import com.moneyapi.moneyapi.db.entity.User
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository


@Transactional
interface UserRepository : JpaRepository<User, Long> {
    fun findByemail(email: String): User
}