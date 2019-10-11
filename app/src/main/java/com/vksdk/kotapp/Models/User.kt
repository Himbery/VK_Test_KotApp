package com.vksdk.kotapp.Models

import org.json.JSONObject
import java.util.*

data class User(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val image: String = "",
    val deactivated: Boolean = false

) {


    companion object CREATOR {

        fun parse(json: JSONObject) = User(id = json.optInt("id", 0),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            image = json.optString("photo_100", ""),
            deactivated = json.optBoolean("deactivated", false))

    }
}