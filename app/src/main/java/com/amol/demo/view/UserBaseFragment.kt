package com.amol.demo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amol.demo.Constant
import com.amol.demo.R
import com.amol.demo.view.adapter.UsersListAdapter
import com.amol.demo.viewmodel.UsersListViewModel


open class UserBaseFragment : Fragment() {

    lateinit var userListViewModel: UsersListViewModel
    lateinit var usersAdapter: UsersListAdapter
    private lateinit var itemViewer : RecyclerView
    private lateinit var emptyView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.detail_fragment, container, false)
        usersAdapter = UsersListAdapter(listOf(), null)
        userListViewModel = ViewModelProvider(requireActivity()).get(UsersListViewModel::class.java)
        itemViewer = view.findViewById<RecyclerView>(R.id.recyclerView)
        emptyView = view.findViewById(R.id.empty_view) as TextView
        itemViewer.layoutManager = LinearLayoutManager(context)
        itemViewer.adapter = usersAdapter
        readArgument(arguments)
        observeData()
        getData()

        return view
    }

    open fun readArgument(arguments: Bundle?) {

    }

    open fun getData() {

    }

    open fun observeData() {

    }

    fun refreshView(size: Int) {
        if(size ==0 ) {
            emptyView.visibility = View.VISIBLE
            itemViewer.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            itemViewer.visibility = View.VISIBLE
        }
    }
}
