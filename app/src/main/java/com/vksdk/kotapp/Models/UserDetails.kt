package com.vksdk.kotapp.Models

import org.json.JSONObject

class UserDetails (
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val bigImage: String = "",
    val status: Int = 0,
    val gender: Int = 0,
    val homeCity: String = ""
) {


    companion object CREATOR {

        fun parse(json: JSONObject) = UserDetails(
            id = json.optInt("id", 0),
            bigImage = json.optString("photo_max", ""),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            gender = json.optInt("sex", 0),
            status = json.optInt("relation", 0),
            homeCity = json.optString("home_town", "")
        )
    }
}
