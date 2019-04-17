package com.moneyapi.moneyapi.security

import org.springframework.http.ResponseEntity
import com.moneyapi.moneyapi.security.jwt.JwtAuthenticationRequest
import com.moneyapi.moneyapi.security.jwt.JwtTokenUtil
import com.moneyapi.moneyapi.service.UserService
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.AuthenticationException

@RestController
@CrossOrigin(origins = ["*"])
class AuthenticationRestController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Qualifier("jwtUserDetailsServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private val userService: UserService? = null

    @PostMapping(value = ["/api/auth"])
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtAuthenticationRequest): ResponseEntity<*> {

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.email,
                        authenticationRequest.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.email)
        val token = jwtTokenUtil.generateToken(userDetails)
        val user = userService?.findByEmail(authenticationRequest.email!!)
        user?.password = null
        return ResponseEntity.ok(CurrentUser(token, user))
    }

    @PostMapping(value = ["/api/refresh"])
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): ResponseEntity<*> {
        val token = request.getHeader("Authorization")
        val username = jwtTokenUtil.getUsernameFromToken(token)
        val user = userService?.findByEmail(username!!)

        return if (jwtTokenUtil.canTokenBeRefreshed(token)!!) {
            val refreshedToken = jwtTokenUtil.refreshToken(token)
            ResponseEntity.ok(CurrentUser(refreshedToken, user))
        } else {
            ResponseEntity.badRequest().body<Any>(null)
        }
    }
}
