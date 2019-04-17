package com.moneyapi.moneyapi.db.entity

import com.moneyapi.moneyapi.security.ProfileEnum
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
        @Id @GeneratedValue var id: Long,
        var email: String,
        var password: String?,
        var profile: ProfileEnum
) : Serializable