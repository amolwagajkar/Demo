package com.amol.demo.db.paging

import androidx.paging.DataSource
import com.amol.demo.db.UserDao
import com.amol.demo.db.entity.User

class DataBaseDataSourceFactory(dao: UserDao) :
    DataSource.Factory<String, User>() {
    private val pageKeyedDataSource: DataBasePageKeyedDataSource =
        DataBasePageKeyedDataSource(dao)

    override fun create(): DataSource<String, User> {
        return pageKeyedDataSource
    }

}