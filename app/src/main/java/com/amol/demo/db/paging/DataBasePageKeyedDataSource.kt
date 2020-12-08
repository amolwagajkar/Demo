package com.amol.demo.db.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amol.demo.db.UserDao
import com.amol.demo.db.entity.User

class DataBasePageKeyedDataSource(private val userDao: UserDao) :
    PageKeyedDataSource<String, User>() {
    var searchText: String = ""
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, User>
    ) {
        Log.i(
            TAG,
            "Loading Initial Rang, Count " + params.requestedLoadSize
        )
        val uses: List<User> = userDao.getUsers(searchText)
        if (uses.isNotEmpty()) {
            callback.onResult(uses, "0", "1")
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, User>
    ) {
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, User>
    ) {
    }

    companion object {
        val TAG = DataBasePageKeyedDataSource::class.java.simpleName
    }

}