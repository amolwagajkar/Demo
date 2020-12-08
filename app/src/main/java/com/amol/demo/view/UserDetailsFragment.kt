package com.amol.demo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.amol.demo.Constant.Companion.FOLLOWERS
import com.amol.demo.Constant.Companion.FOLLOWERS_URL
import com.amol.demo.Constant.Companion.FOLLOWINGS
import com.amol.demo.Constant.Companion.FOLLOWING_URL
import com.amol.demo.R
import com.amol.demo.view.adapter.MyAdapter
import com.google.android.material.tabs.TabLayout


class UserDetailsFragment : Fragment() {

    companion object {
        val TAG = UserDetailsFragment::class.java.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.user_details, container, false)

        activity?.supportFragmentManager?.let {
            var followers = ""
            var followings = ""
            val adapter = MyAdapter(it)

            arguments?.let {
                it.getString(FOLLOWERS_URL)?.let {
                    followers = it
                }
                it.getString(FOLLOWING_URL)?.let {
                    followings = it
                }
            }

            val followersListFragment = FollowersListFragment()
            var followersBundle = Bundle()
            followersBundle.putString(FOLLOWERS_URL, followers)
            followersListFragment.arguments = followersBundle

            val followingListFragment = FollowingListFragment()
            var followingBundle = Bundle()
            followingBundle.putString(FOLLOWING_URL, followings)
            followingListFragment.arguments = followingBundle

            adapter.addFragment(followersListFragment, FOLLOWERS)
            adapter.addFragment(followingListFragment, FOLLOWINGS)
            val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
            viewPager.adapter = adapter
            val tabs = view.findViewById<TabLayout>(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        }
        return view
    }
}
