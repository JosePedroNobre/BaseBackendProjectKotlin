package com.moneyapi.moneyapi.controller

import com.moneyapi.moneyapi.db.entity.User
import com.moneyapi.moneyapi.security.ProfileEnum
import com.moneyapi.moneyapi.service.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.ObjectError
import org.springframework.validation.BindingResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = ["*"])
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostMapping
    fun create(request: HttpServletRequest, @RequestBody user: User,
               result: BindingResult): ResponseEntity<Response<User>> {

        val response = Response<User>()

        try {
            validateCreateUser(user, result)
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body<Response<User>>(response)
            }

            user.password = passwordEncoder.encode(user.password)
            user.profile = ProfileEnum.USER

            val userPersisted = userService.createOrUpdate(user)
            response.data = userPersisted

        } catch (dE: DuplicateKeyException) {
            response.errors = listOf("E-mail already registered !")
            return ResponseEntity.badRequest().body<Response<User>>(response)

        } catch (e: Exception) {
            return ResponseEntity.badRequest().body<Response<User>>(response)
        }

        return ResponseEntity.ok<Response<User>>(response)
    }

    private fun validateCreateUser(user: User, result: BindingResult) {
        if (user.email.isEmpty()) {
            result.addError(ObjectError("User", "Email no information"))
        }
    }
}