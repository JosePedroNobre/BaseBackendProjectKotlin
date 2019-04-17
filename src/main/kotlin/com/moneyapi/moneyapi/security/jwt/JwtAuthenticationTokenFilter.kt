package com.moneyapi.moneyapi.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import javax.servlet.ServletException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

    @Qualifier("jwtUserDetailsServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    private var username = ""

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authToken = request.getHeader("Authorization")
        if (authToken != null) {
            username = jwtTokenUtil.getUsernameFromToken(authToken)
        } else {
            username = jwtTokenUtil.getUsernameFromToken("xc")
        }

        if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userDetailsService.loadUserByUsername(username)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                logger.info("authenticated user $username, setting security context")
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }
}