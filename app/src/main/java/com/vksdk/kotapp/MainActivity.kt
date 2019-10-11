package com.vksdk.kotapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vk.api.sdk.*
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vksdk.kotapp.Models.User
import com.vksdk.kotapp.Services.UserManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.widget.SwipeRefreshLayout
import com.vk.api.sdk.requests.VKRequest


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    val adapter = UsersAdapter()

    val userManager: UserManager = UserManager()

    var isLoading: Boolean = false
    val count: Int = 20
    var offset: Int = 0
    var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.WALL))

        cancel_btn.setOnClickListener(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layout: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layout.itemCount
                val lastItemPosition = layout.findLastVisibleItemPosition()
                if ((lastItemPosition >= totalItemCount - 1) && !isLoading) {
                   searchUser(searchQuery)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

        searchView = findViewById<SearchView>(R.id.searchview)
        searchView.setIconifiedByDefault(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query

                searchUser(query)

                return false
            }
        })

        pullToRefresh = findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            offset = 0

            searchUser(searchQuery)
            pullToRefresh.isRefreshing = false
        }
    }
    override fun onClick(v: View?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
            }
            override fun onLoginFailed(errorCode: Int) {
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun searchUser(query: String) {
        isLoading = true
        userManager.search(query, offset, count, { users -> Unit
            if(users.isEmpty()){
                Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_LONG).show();
                return@search
            }

            if (offset > 0) {
                adapter.addData(users)
            } else {
                adapter.setData(users)
                recyclerView.adapter = adapter
            }

            adapter.setOnItemClickListener(object : UsersAdapter.OnItemClickListener{
                override fun onItemClick(user: User) {
                    val intent = Intent (this@MainActivity, UsersDetailsActivity :: class.java)
                    intent.putExtra("id", user.id)
                    startActivity(intent)
                }
            })

            offset = offset + count
            isLoading = false
        })
    }
}

