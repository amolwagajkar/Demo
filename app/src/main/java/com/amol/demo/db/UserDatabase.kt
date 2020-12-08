package com.amol.demo.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amol.demo.db.entity.User
import com.amol.demo.db.paging.DataBaseDataSourceFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    private lateinit var usersPaged: LiveData<PagedList<User>>
    private val EXECUTOR_THREAD_COUNT = 5
    private var searchText: String = ""

    companion object {
        private var instance: UserDatabase? = null
        private const val deviceDatabaseName: String = "user_database"

        fun getInstance(context: Context): UserDatabase {
            if (instance == null) {
                synchronized(UserDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        deviceDatabaseName
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    instance?.initialize()
                }
            }
            return instance!!
        }
    }

    fun initialize() {
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Int.MAX_VALUE)
                .setPageSize(Int.MAX_VALUE).build()
        val executor: Executor =
            Executors.newFixedThreadPool(EXECUTOR_THREAD_COUNT)
        val dataSourceFactory =
            DataBaseDataSourceFactory(
                userDao()
            )
        val livePagedListBuilder: LivePagedListBuilder<String, User> =
            LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        usersPaged =
            livePagedListBuilder.setFetchExecutor(executor).build()
    }

    open fun getUsers(search: String): LiveData<PagedList<User>> {
        searchText = search
        return usersPaged
    }
}