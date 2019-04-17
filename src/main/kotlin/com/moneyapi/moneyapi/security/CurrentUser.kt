package com.moneyapi.moneyapi.security

import com.moneyapi.moneyapi.db.entity.User

class CurrentUser(var token: String?, var user: User?)