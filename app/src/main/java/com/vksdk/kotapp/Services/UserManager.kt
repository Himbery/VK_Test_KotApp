package com.vksdk.kotapp.Services

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import com.vksdk.kotapp.MainActivity
import com.vksdk.kotapp.Models.User
import com.vksdk.kotapp.Models.UserDetails
import com.vksdk.kotapp.R
import com.vksdk.kotapp.UsersDetailsActivity
import kotlinx.android.synthetic.main.users_detail_activity.*
import org.intellij.lang.annotations.Language
import org.json.JSONObject

class UserManager {
    fun search(query: String, offset: Int, count: Int,  completion: (users: List<User>) -> Unit) {
        VK.execute(VKUserSearchRequest(query, offset, count), object : VKApiCallback<List<User>> {
            override fun success(result: List<User>) {
                completion(result)
            }
            override fun fail(error: VKApiExecutionException) {
            }
        })
    }

    fun userInfo(userId: Int, completion: (user: UserDetails) -> Unit) {
        VK.execute(VKUserRequest(userId), object : VKApiCallback<UserDetails>{
            override fun success(result: UserDetails) {
                completion(result)
            }
            override fun fail(error: VKApiExecutionException) {
            }
        })
    }

    private class VKUserSearchRequest : VKRequest<List<User>> {
        constructor(query: String, offset: Int, count: Int) : super("users.search") {
            if (query.isNotEmpty()) {
                addParam("q", query)
                addParam("fields", "photo_100")
                addParam("offset", offset)
                addParam("count", count)
            }
        }

        override fun parse(r: JSONObject): List<User> {
            val response = r.getJSONObject("response")
            val users = response.getJSONArray("items")
            val result = ArrayList<User>()
            for (i in 0 until users.length()) {
                result.add(User.parse(users.getJSONObject(i)))
            }
            return result
        }
    }

    private class VKUserRequest : VKRequest<UserDetails> {

        val FIELDS = "photo, photo_max, home_town, sex, relation"
        constructor(userId: Int) : super("users.get") {
            if (userId != 0) {
                addParam("user_ids", userId)
                addParam("fields", FIELDS)
            }

        }

        override fun parse(r: JSONObject): UserDetails {
            val res = r.getJSONArray("response")
            return UserDetails.parse(res.getJSONObject(0))
        }
    }
}
