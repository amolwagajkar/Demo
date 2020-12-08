package com.amol.demo.view

import android.os.Bundle
import com.amol.demo.Constant

class FollowingListFragment : UserBaseFragment() {

    var followings = ""

    override fun readArgument(arguments: Bundle?) {
        arguments?.let {
            it.getString(Constant.FOLLOWING_URL)?.let {
                followings = it
            }
        }
    }

    override fun observeData() {

        userListViewModel.followingList.observe(viewLifecycleOwner) {
            usersAdapter.userList = it
            usersAdapter.notifyDataSetChanged()
            refreshView(it.size)
        }
    }

    override fun getData() {
        userListViewModel.getUserFollowingList(followings)
    }

}
