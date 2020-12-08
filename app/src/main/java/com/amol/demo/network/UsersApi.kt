package com.amol.demo.network

import com.amol.demo.db.entity.User
import com.amol.demo.model.UserResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface UsersApi {
    @GET("search/users?")
    fun getUsers(
        @Query("q") searchText: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Call<UserResponseModel>

    @GET("{url}")
    fun getUsersList(@Path(value = "url", encoded = true) url: String): Call<List<User>>
}