package com.vksdk.kotapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vksdk.kotapp.Models.User

class UsersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val users: MutableList<User> = arrayListOf()
    lateinit var mClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    fun setOnItemClickListener(aClickListener: OnItemClickListener) {
        mClickListener = aClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserHolder(parent.context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserHolder).bind(users[position], mClickListener)
    }

    fun addData(users: List<User>) {
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun setData(users: List<User>) {
        this.users.clear()

        addData(users)
    }

    override fun getItemCount() = users.size

    inner class UserHolder(context: Context?): RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_user, null)) {

        private val avatarVK: ImageView = itemView.findViewById(R.id.avatarVK)
        private val nameVK: TextView = itemView.findViewById(R.id.nameVK)

        fun bind(user: User,  mClickListener: OnItemClickListener) {
            itemView.setOnClickListener{
                mClickListener.onItemClick(user)
            }
            nameVK.text = "${user.firstName} ${user.lastName}"
            if (!TextUtils.isEmpty(user.image)) {
                Picasso.get().load(user.image).error(R.drawable.user_placeholder).into(avatarVK)
            } else {
                avatarVK.setImageResource(R.drawable.user_placeholder)
            }
        }
    }
}
