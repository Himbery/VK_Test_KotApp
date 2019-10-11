package com.vksdk.kotapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import com.vksdk.kotapp.Models.User
import com.vksdk.kotapp.Models.UserDetails
import com.vksdk.kotapp.Services.UserManager
import kotlinx.android.synthetic.main.users_detail_activity.*
import org.json.JSONArray
import org.json.JSONObject

class UsersDetailsActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnBack: ImageView
    var usersId: Int = 0

    val userManager: UserManager = UserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_detail_activity)

        btnBack = findViewById(R.id.back_btn)
        btnBack.setOnClickListener(this)

        usersId = intent.getIntExtra("id", 0)
        fetchUser(usersId)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun fetchUser(userId: Int) {
        userManager.userInfo(userId, { user -> Unit
            if (user != null) {
                val nameTV = findViewById<TextView>(R.id.userTV)
                nameTV.text = "${user.firstName} ${user.lastName}"
                title_name.text = "${user.firstName} ${user.lastName}"

                val genderTv = findViewById<TextView>(R.id.genderTV)
                val gender: String = "${user.gender}"
                if(gender == "1"){
                    genderTv.text = "женский"
                } else if(gender == "2"){
                    genderTv.text = "мужской"
                } else {
                    genderTv.text = "пол не указан"
                }

                val homeCity = findViewById<TextView>(R.id.cityTV)
                homeCity.text = "${user.homeCity}"

                val statusTV = findViewById<TextView>(R.id.statusTV)
                val status: String = "${user.status}"
                if(status == "0"){
                    statusTV.text = "не указано"
                } else if(status == "1" && gender == "1") {
                    statusTV.text = "не замужем"
                } else if(status == "1" && gender == "2"){
                    statusTV.text = "не женат"
                } else if(status == "2" && gender == "1"){
                    statusTV.text = "есть друг"
                } else if(status == "2" && gender == "2"){
                    statusTV.text = "есть подруга"
                } else if(status == "3" && gender == "1"){
                    statusTV.text = "помолвлена"
                } else if(status == "3" && gender == "2"){
                    statusTV.text = "помолвлен"
                } else if(status == "4" && gender == "1"){
                    statusTV.text = "замужем"
                } else if(status == "4" && gender == "2"){
                    statusTV.text = "женат"
                } else if(status == "5" ){
                    statusTV.text = "всё сложно"
                } else if(status == "6"){
                    statusTV.text = "в активном поиске"
                } else if(status == "7" && gender == "1"){
                    statusTV.text = "влюблена"
                } else if(status == "7" && gender == "2"){
                    statusTV.text = "влюблен"
                } else {
                    statusTV.text = "в гражданском браке"
                }

                val avatarIV = findViewById<ImageView>(R.id.photo_image)
                if (!TextUtils.isEmpty(user.bigImage)) {
                    Picasso.get().load(user.bigImage).error(R.drawable.user_placeholder).into(avatarIV)
                } else {
                    avatarIV.setImageResource(R.drawable.user_placeholder)
                }
            }
        })
    }
}
