package com.moneyapi.moneyapi.security.jwt

import com.moneyapi.moneyapi.db.entity.User
import com.moneyapi.moneyapi.security.ProfileEnum
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.ArrayList
import org.springframework.security.core.GrantedAuthority

object JwtUserFactory {

    fun create(user: User): JwtUser {
        return JwtUser(user.id,
                user.email,
                user.password!!,
                mapToGrantedAuthorities(user.profile))
    }

    private fun mapToGrantedAuthorities(profileEnum: ProfileEnum): List<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(profileEnum.toString()))
        return authorities
    }
}