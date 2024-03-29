package com.moneyapi.moneyapi.security.jwt

import java.io.IOException
import java.io.Serializable

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {

    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest,
                          response: HttpServletResponse, authException: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}