package com.example.accountmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.accountmanager.ModelClassess.Users

class user_Adapter(private val context: Context, private var dataSource: List<Users>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.user_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.username),
                view.findViewById(R.id.email),
                view.findViewById(R.id.phone),
                view.findViewById(R.id.user_image)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val account = getItem(position) as Users
        holder.usernameTextView.text = account.username
        holder.emailTextView.text = account.email
        holder.phoneTextView.text = account.phone
        Glide.with(context).load(account.imageUrl).placeholder(R.drawable.baseline_account_circle_24).into(holder.userImageView)

        return view
    }

    fun updateData(newData: List<Users>) {
        dataSource = newData
        notifyDataSetChanged()
    }

    private class ViewHolder(
        val usernameTextView: TextView,
        val emailTextView: TextView,
        val phoneTextView: TextView,
        val userImageView: ImageView
    )
}
