package com.github.rednit

import androidx.lifecycle.ViewModel
import com.rednit.tinder4j.api.TinderClient
import com.rednit.tinder4j.api.entities.user.LikePreview
import com.rednit.tinder4j.exceptions.LoginException


class TinderConnection : ViewModel() {

    companion object Client {
        val connection = TinderConnection()
    }

    var token = ""

    private lateinit var client: TinderClient
    private var likePreviews = emptyList<LikePreview>()
    private var likePreviewsFetched = false

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

    fun teaserCount(): Int {
        return client.likeCount.complete()
    }

    // 10 is the maximum amount of teasers the api sends
    fun likePreviewCount(): Int {
        return if (!likePreviewsFetched) 10 else 5
        //return if (!likePreviewsFetched) 10 else likePreviews.size
    }

    fun likePreviews(): List<LikePreview> {
        if (!likePreviewsFetched) {
            likePreviews = client.likePreviews.complete()
            likePreviewsFetched = true
        }
        return likePreviews
    }
}