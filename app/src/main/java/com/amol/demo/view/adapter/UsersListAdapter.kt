package com.amol.demo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.amol.demo.R
import com.amol.demo.db.entity.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UsersListAdapter(
    var userList: List<User>,
    val listener: UsersListAdapterInteraction?
) : RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder>() {

    lateinit var context: Context

    interface UsersListAdapterInteraction {
        fun onUserItemClick(user: User)
    }

    inner class UsersListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userItem = itemView.findViewById<LinearLayout>(R.id.userItem)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val txtName: AppCompatTextView = itemView.findViewById(R.id.textViewName)
        val txtType: AppCompatTextView = itemView.findViewById(R.id.textViewType)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val user = userList[position]
        user?.let {
            Glide.with(context)
                .load(it.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar)

            holder.txtName.text = it.name
            holder.txtType.text = it.type

            holder.userItem.setOnClickListener {
                listener?.onUserItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.user_list_item, parent, false)
        return UsersListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}