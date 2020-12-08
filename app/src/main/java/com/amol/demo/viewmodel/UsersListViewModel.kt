package com.amol.demo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amol.demo.db.entity.User
import com.amol.demo.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class UsersListViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UsersRepository = UsersRepository(application.applicationContext)
    var userList = MutableLiveData<List<User>>()
    var followerList = MutableLiveData<List<User>>()
    var followingList = MutableLiveData<List<User>>()

    fun getUsers(searchText: String, loadMore: Boolean) {
        GlobalScope.async {
            withContext(Dispatchers.IO) {
                if (loadMore) {
                    val existingList = mutableListOf<User>()
                    userList.value?.let {
                        existingList.addAll(it)
                    }
                    repository.getUsers(searchText, loadMore).let {
                        existingList.addAll(it)
                        userList.postValue(existingList)
                    }
                } else {
                    userList.postValue(repository.getUsers(searchText, loadMore))
                }
            }
        }
    }

    fun getUserFollowerList(url: String) {
        GlobalScope.async {
            withContext(Dispatchers.IO) {
                repository.getUsersList(url).let {
                    followerList.postValue(it)
                }
            }
        }
    }

    fun getUserFollowingList(url: String) {
        GlobalScope.async {
            withContext(Dispatchers.IO) {
                repository.getUsersList(url).let {
                    followingList.postValue(it)
                }
            }
        }
    }

    fun resetUserLists() {
        followerList.value = listOf()
        followingList.value = listOf()
    }

}
