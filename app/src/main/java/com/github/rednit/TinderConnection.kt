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
    private var teaserCount = 0
    private var teaserCountFetched = false
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

    fun resetCache() {
        teaserCountFetched = false
        likePreviewsFetched = false
    }

    fun teaserCount(): Int {
        if (!teaserCountFetched) {
            teaserCount = client.likeCount.complete()
            teaserCountFetched = true
        }
        return teaserCount
    }

    fun likePreviewCount(): Int {
        return if (!likePreviewsFetched) 0 else likePreviews.size
    }

    fun likePreviews(): List<LikePreview> {
        if (!likePreviewsFetched) {
            likePreviews = client.likePreviews.complete()
            likePreviewsFetched = true
        }
        return likePreviews
    }
}