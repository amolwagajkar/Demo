package com.amol.demo.model

import com.amol.demo.db.entity.User
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("total_count") var totalCount: Long,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var items: List<User>
)