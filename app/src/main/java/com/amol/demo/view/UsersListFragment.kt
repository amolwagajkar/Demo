package com.amol.demo.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amol.demo.Constant.Companion.FOLLOWERS_URL
import com.amol.demo.Constant.Companion.FOLLOWING_URL
import com.amol.demo.Constant.Companion.SEARCH_APPBAR
import com.amol.demo.Constant.Companion.STANDARD_APPBAR
import com.amol.demo.R
import com.amol.demo.db.entity.User
import com.amol.demo.service.RetrofitService
import com.amol.demo.view.UserDetailsFragment.Companion.TAG
import com.amol.demo.view.adapter.UsersListAdapter
import com.amol.demo.viewmodel.UsersListViewModel
import com.google.android.material.appbar.AppBarLayout
import java.util.*


class UsersListFragment : Fragment(), UsersListAdapter.UsersListAdapterInteraction {

    private lateinit var usersAdapter: UsersListAdapter
    private lateinit var userListViewModel: UsersListViewModel
    private var mAppBarState = 0

    lateinit var searchBar: AppBarLayout
    lateinit var viewContactsBar: AppBarLayout
    var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.users_fragment, container, false)
        userListViewModel = ViewModelProvider(requireActivity()).get(UsersListViewModel::class.java)
        usersAdapter = UsersListAdapter(listOf(), this)


        viewContactsBar = view.findViewById(R.id.viewContactsToolbar)
        searchBar = view.findViewById(R.id.searchToolbar)
        val searchContacts = view.findViewById<EditText>(R.id.etSearchContacts)
        searchContacts.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val text = searchContacts.getText().toString().toLowerCase(Locale.getDefault())
                getUsers(text, false)
            }
        })

        setAppBaeState(STANDARD_APPBAR)

        val searchContact: ImageView = view.findViewById<View>(R.id.ivSearchIcon) as ImageView
        searchContact.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "onClick: clicked searched icon")
            toggleToolBarState()
        })

        val backArrow: ImageView = view.findViewById<View>(R.id.ivBackArrow) as ImageView
        backArrow.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "onClick: clicked back arrow.")
            toggleToolBarState()
        })

        val itemViewer = view.findViewById<RecyclerView>(R.id.recycler)
        itemViewer.layoutManager = LinearLayoutManager(context)
        itemViewer.adapter = usersAdapter

        itemViewer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == usersAdapter.itemCount - 1) {
                        isLoading = true
                        getUsers(
                            searchContacts.getText().toString().toLowerCase(Locale.getDefault()),
                            true
                        )
                    }
                }
            }
        })

        userListViewModel.userList.observe(viewLifecycleOwner) {
            usersAdapter.userList = it
            usersAdapter.notifyDataSetChanged()
            isLoading = false
        }

        getUsers("", false)
        return view
    }


    private fun getUsers(search: String, loadMore: Boolean) {
        userListViewModel.getUsers(search, loadMore)
    }

    override fun onUserItemClick(user: User) {
        val transaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(TAG)
        transaction?.let {
            userListViewModel.resetUserLists()
            var fragment = UserDetailsFragment()
            var followersUrl = user.followersUrl
            var followingUrl = user.followingUrl
            followersUrl = followersUrl.replace(RetrofitService.BASE_URL, "")
            followingUrl = followingUrl.replace(RetrofitService.BASE_URL, "")
            var bundle = Bundle()
            bundle.putString(FOLLOWERS_URL, followersUrl)
            bundle.putString(FOLLOWING_URL, followingUrl)
            fragment.arguments = bundle
            transaction.add(R.id.app_main_fragment_container, fragment)
            transaction.commit()
        }
    }

    private fun toggleToolBarState() {
        Log.d(TAG, "toggleToolBarState: toggling AppBarState.")
        if (mAppBarState === STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR)
        } else {
            setAppBaeState(STANDARD_APPBAR)
        }
    }

    // Sets the appbar state for either search mode or standard mode.
    private fun setAppBaeState(state: Int) {
        Log.d(TAG, "setAppBaeState: changing app bar state to: $state")
        mAppBarState = state
        if (mAppBarState === STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE)
            viewContactsBar.setVisibility(View.VISIBLE)
            val view = view
            val im: InputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                im.hideSoftInputFromWindow(view!!.windowToken, 0) // make keyboard hide
            } catch (e: NullPointerException) {
                Log.d(TAG, "setAppBaeState: NullPointerException: $e")
            }
        } else if (mAppBarState === SEARCH_APPBAR) {
            viewContactsBar.setVisibility(View.GONE)
            searchBar.setVisibility(View.VISIBLE)
            val im: InputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0) // make keyboard popup
        }
    }
}
