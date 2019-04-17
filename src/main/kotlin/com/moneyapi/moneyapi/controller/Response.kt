package com.moneyapi.moneyapi.controller

class Response<T> {

    var data: T? = null
    var dataArray: List<T>? = null
    var errors: List<String>? = null
}