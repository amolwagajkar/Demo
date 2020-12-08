package com.amol.demo.network.paging

import androidx.paging.DataSource
import com.amol.demo.db.entity.User
import rx.subjects.ReplaySubject


class NetworkDataSourceFactory() : DataSource.Factory<Int, User>() {

    private var pageKeyedDataSource: NetworkDataSource = NetworkDataSource()


    override fun create(): DataSource<Int, User> {
        return pageKeyedDataSource
    }


    fun getUsers(search: String): ReplaySubject<User> {
        return pageKeyedDataSource.getUsers(search)
    }

}