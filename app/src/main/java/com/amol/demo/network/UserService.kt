package com.amol.demo.network

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.BoundaryCallback
import com.amol.demo.db.entity.User
import com.amol.demo.network.paging.NetworkDataSource
import com.amol.demo.network.paging.NetworkDataSourceFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class UserService(
    val dataSourceFactory: NetworkDataSourceFactory,
    val boundaryCallback: BoundaryCallback<User>
) {
    lateinit var pagedUsers: LiveData<PagedList<User>>
    private val EXECUTOR_THREAD_COUNT = 5

    companion object {
        private val TAG = UserService::class.java.simpleName
    }

    init {
        refreshPagedUser()
    }

    fun refreshPagedUser() {
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false)
                .setInitialLoadSizeHint(NetworkDataSource.PAGE_SIZE)
                .setPageSize(NetworkDataSource.PAGE_SIZE).build()
        val executor: Executor =
            Executors.newFixedThreadPool(EXECUTOR_THREAD_COUNT)
        val livePagedListBuilder: LivePagedListBuilder<Int, User> =
            LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        pagedUsers =
            livePagedListBuilder.setFetchExecutor(executor).setBoundaryCallback(boundaryCallback)
                .build()
    }
}