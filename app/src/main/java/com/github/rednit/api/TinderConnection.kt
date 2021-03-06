package com.github.rednit.api

import androidx.lifecycle.ViewModel
import com.rednit.tinder4j.api.TinderClient
import com.rednit.tinder4j.api.entities.Match
import com.rednit.tinder4j.api.entities.user.LikePreview
import com.rednit.tinder4j.api.entities.user.SelfUser
import com.rednit.tinder4j.api.entities.user.swipeable.Recommendation
import com.rednit.tinder4j.exceptions.LoginException
import com.yuyakaido.android.cardstackview.Direction
import java.util.*


class TinderConnection : ViewModel() {

    companion object Client {
        val connection = TinderConnection()
    }

    var token = ""

    lateinit var client: TinderClient
    private var rateLimiter = AppRateLimiter()
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
        client.ratelimiter = rateLimiter
        return true
    }

    fun endLoginPhase() {
        rateLimiter.loginPhase = false
    }

    fun recommendations(): List<Recommendation> {
        return client.recommendations.complete()
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

    fun getMatch(id: String): Match {
        return client.matchCacheView.getMatch(id).complete()
    }

    fun getSelfUser(): SelfUser {
        return client.selfUser.complete()
    }

    fun swipe(direction: Direction, user: Recommendation) : Optional<String> {
        if (direction == Direction.Left) {
            user.dislike().complete()
            return Optional.empty()
        } else if (direction == Direction.Right) {
            return user.like().complete()
        }
        return Optional.empty()
    }

}