package com.github.rednit.api

import com.rednit.tinder4j.requests.DefaultRatelimiter
import com.rednit.tinder4j.requests.Request

class AppRateLimiter : DefaultRatelimiter() {

    var loginPhase = true

    override fun shouldDelay(request: Request<*>?): Boolean {
        return if (loginPhase) {
            false
        } else {
            super.shouldDelay(request)
        }
    }
}
