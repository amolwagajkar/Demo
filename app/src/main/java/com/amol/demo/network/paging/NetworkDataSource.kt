package com.amol.demo.network.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amol.demo.db.entity.User
import com.amol.demo.model.UserResponseModel
import com.amol.demo.network.UsersApi
import com.amol.demo.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.subjects.ReplaySubject
import java.util.*

class NetworkDataSource : PageKeyedDataSource<Int, User>() {
    private val TAG = NetworkDataSource::class.java.simpleName
    private var usersObservable: ReplaySubject<User> = ReplaySubject.create()
    var searchText: String = "repos:>1"

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        val service: UsersApi = RetrofitService.createService(UsersApi::class.java)
        val call = service.getUsers(searchText, params.key, PAGE_SIZE)
        call.enqueue(object : Callback<UserResponseModel> {
            override fun onResponse(
                call: Call<UserResponseModel>,
                response: Response<UserResponseModel>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val key = if (params.key > 1) params.key - 1 else 0
                    apiResponse?.let {
                        callback.onResult(apiResponse.items, key)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
            }
        })
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int,
                User>
    ) {
        val service: UsersApi = RetrofitService.createService(UsersApi::class.java)
        val call = service.getUsers(searchText, FIRST_PAGE, PAGE_SIZE)
        call.enqueue(object : Callback<UserResponseModel> {
            override fun onResponse(
                call: Call<UserResponseModel>,
                response: Response<UserResponseModel>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    apiResponse?.let {
                        callback.onResult(apiResponse.items, null, FIRST_PAGE + 1)
                        var userResponseModel = response.body()!!
                        for (user in userResponseModel.items) {
                            usersObservable.onNext(user)
                        }
                    }
                } else {
                    Log.e(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                callback.onResult(ArrayList(), 1, 2)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        val service: UsersApi = RetrofitService.createService(UsersApi::class.java)
        val call = service.getUsers(searchText, params.key, 10)
        call.enqueue(object : Callback<UserResponseModel> {
            override fun onResponse(
                call: Call<UserResponseModel>,
                response: Response<UserResponseModel>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val key = params.key + 1
                    apiResponse?.let {
                        callback.onResult(apiResponse.items, key)
                        var userResponseModel = response.body()!!
                        for (user in userResponseModel.items) {
                            usersObservable.onNext(user)
                        }
                    }
                } else {
                    Log.e(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                callback.onResult(ArrayList(), params.key)
            }
        })
    }

    fun getUsers(search: String): ReplaySubject<User> {
        searchText = if (search.isEmpty()) "repos:>1" else search
        return usersObservable
    }


    companion object {
        const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
    }
}