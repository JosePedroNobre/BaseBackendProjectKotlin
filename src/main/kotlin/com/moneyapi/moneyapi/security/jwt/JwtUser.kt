package com.moneyapi.moneyapi.security.jwt

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUser(@get:JsonIgnore
              val id: Long?, private val
              username: String,
              private val password: String,
              private val authorities: Collection<GrantedAuthority>)
    : UserDetails {

    override fun getUsername() = username

    @JsonIgnore
    override fun isAccountNonExpired() = true

    @JsonIgnore
    override fun isAccountNonLocked() = true

    @JsonIgnore
    override fun isCredentialsNonExpired() = true

    @JsonIgnore
    override fun getPassword() = password

    override fun getAuthorities() = authorities

    override fun isEnabled() = true

    companion object {
        private val serialVersionUID = 1L
    }
}