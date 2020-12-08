package com.amol.demo.view

import android.os.Bundle
import com.amol.demo.Constant


class FollowersListFragment : UserBaseFragment() {

    var followers = ""

    override fun readArgument(arguments: Bundle?) {
        arguments?.let {
            it.getString(Constant.FOLLOWERS_URL)?.let {
                followers = it
            }
        }
    }

    override fun observeData() {
        userListViewModel.followerList.observe(viewLifecycleOwner) {
            usersAdapter.userList = it
            usersAdapter.notifyDataSetChanged()
            refreshView(it.size)
        }
    }

    override fun getData() {
        userListViewModel.getUserFollowerList(followers)
    }

}
