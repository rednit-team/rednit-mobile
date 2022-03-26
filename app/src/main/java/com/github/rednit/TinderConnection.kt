package com.github.rednit

import com.rednit.tinder4j.api.TinderClient
import com.rednit.tinder4j.exceptions.LoginException


object TinderConnection {

    lateinit var client: TinderClient
    var token: String = ""

     fun login(): Boolean {
        if (token.isBlank()) {
            return false
        }
        try {
            client = TinderClient(token)
        } catch (ignored: LoginException) {
            ignored.printStackTrace()
            return false
        }
        return true
    }

}