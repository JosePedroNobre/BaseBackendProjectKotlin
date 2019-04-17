package com.moneyapi.moneyapi.security.jwt

import java.io.Serializable
import java.util.Date
import java.util.HashMap
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

@Component
class JwtTokenUtil : Serializable {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun getUsernameFromToken(token: String): String {
        val username: String
        username = try {
            val claims = getClaimsFromToken(token)
            claims!!.subject
        } catch (e: Exception) {
            ""
        }

        return username
    }

    fun getExpirationDateFromToken(token: String): Date? {
        var expiration: Date?
        try {
            val claims = getClaimsFromToken(token)
            expiration = claims!!.expiration
        } catch (e: Exception) {
            expiration = null
        }
        return expiration
    }

    private fun getClaimsFromToken(token: String): Claims? {
        var claims: Claims?
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            claims = null
        }
        return claims
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration!!.before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        claims[CLAIM_KEY_USERNAME] = userDetails.username

        val createdDate = Date()
        claims[CLAIM_KEY_CREATED] = createdDate

        return doGenerateToken(claims)
    }

    private fun doGenerateToken(claims: MutableMap<String, Any>): String {
        val createdDate = claims[CLAIM_KEY_CREATED] as Date
        val expirationDate = Date(createdDate.time + expiration!! * 1000)
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String): Boolean? {
        return isTokenExpired(token)
    }

    fun refreshToken(token: String): String? {
        var refreshedToken: String?
        try {
            val claims = getClaimsFromToken(token)
            claims!![CLAIM_KEY_CREATED] = Date()
            refreshedToken = doGenerateToken(claims)
        } catch (e: Exception) {
            refreshedToken = null
        }

        return refreshedToken
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as JwtUser
        val username = getUsernameFromToken(token)
        return username == user.username && (!isTokenExpired(token))
    }

    companion object {
        private const val serialVersionUID = -3301605591108950415L
        internal val CLAIM_KEY_USERNAME = "sub"
        internal val CLAIM_KEY_CREATED = "created"
        internal val CLAIM_KEY_EXPIRED = "exp"
    }
}
