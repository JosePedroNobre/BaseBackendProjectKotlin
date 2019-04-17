package com.moneyapi.moneyapi.security

import com.moneyapi.moneyapi.security.jwt.JwtUserFactory
import com.moneyapi.moneyapi.service.UserService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class JwtUserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userService: UserService

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {

        val user = userService.findByEmail(email)
        return JwtUserFactory.create(user)
    }
}