package com.amol.demo.repository

import android.content.Context
import com.amol.demo.db.UserDatabase
import com.amol.demo.db.entity.User
import com.amol.demo.network.NetworkUtil
import com.amol.demo.network.UsersApi
import com.amol.demo.network.paging.NetworkDataSource
import com.amol.demo.service.RetrofitService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLEncoder


class UsersRepository(val context: Context) {
    private var database: UserDatabase = UserDatabase.getInstance(context.applicationContext)
    private var searchText: String = ""
    private val service: UsersApi = RetrofitService.createService(UsersApi::class.java)

    companion object {
        var PAGE_KEY = 1
        val DEFAULT_URL ="repos:>1"
        val ENCODE ="utf-8"
    }

    fun getUsers(search: String, loadMore: Boolean): List<User> {
        var users = mutableListOf<User>()
        if (NetworkUtil.isNetworkAvailable(context)) {
            if (loadMore) PAGE_KEY = PAGE_KEY + 1
            searchText =
                if (search.isEmpty()) DEFAULT_URL else URLEncoder.encode(search, ENCODE)
            var response =
                service.getUsers(searchText, PAGE_KEY, NetworkDataSource.PAGE_SIZE).execute()

            if (response.isSuccessful) {
                val apiResponse = response.body()!!
                apiResponse?.let {
                    var userResponseModel = response.body()!!

                    GlobalScope.launch {
                        //update data in database
                        val dao = database.userDao()
                        for (user in userResponseModel.items) {
                            dao.insertUser(user)
                        }
                    }
                    users.addAll(userResponseModel.items)
                }
            }
        } else {
            var text = search + "%"
            users.addAll(database.userDao().getUsers(text))
        }
        return users
    }

    fun getUsersList(url: String): List<User> {
        var users = mutableListOf<User>()
        var response =
            service.getUsersList(url).execute()
        if (response.isSuccessful) {
            users.addAll(response.body()!!)
        }
        return users
    }

}