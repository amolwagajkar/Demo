package com.amol.demo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amol.demo.db.entity.User


@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE name LIKE :search")
    fun getUsers(search: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

}