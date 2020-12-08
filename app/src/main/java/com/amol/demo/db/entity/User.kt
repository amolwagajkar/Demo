package com.amol.demo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "User")
data class User(

    @PrimaryKey()
    val id: String = "",
    @SerializedName("avatar_url")
    var avatarUrl: String = "",
    @SerializedName("followers_url")
    var followersUrl: String = "",
    @SerializedName("following_url")
    var followingUrl: String = "",

    @SerializedName("login")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    var followers: Long = 0,
    var following: Long = 0
)