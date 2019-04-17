package com.moneyapi.moneyapi.security.jwt

import java.io.Serializable

data class JwtAuthenticationRequest(
        var email: String? = null,
        var password: String? = null)
    : Serializable


