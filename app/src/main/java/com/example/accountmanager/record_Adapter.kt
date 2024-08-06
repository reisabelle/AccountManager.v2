package com.example.accountmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.accountmanager.ModelClassess.Records_Model

class record_Adapter (private val context: Context, private var dataSource: List<Records_Model>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.records_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.tv_subject),
                view.findViewById(R.id.tv_grade),
                view.findViewById(R.id.tv_instructor),
                view.findViewById(R.id.tv_sem)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val record = getItem(position) as Records_Model
        holder.subject.text = record.subject
        holder.grade.text = record.grade
        holder.instructor.text = record.instructor
        holder.semester.text = record.semester

        return view
    }

    fun updateData(newData: List<Records_Model>) {
        dataSource = newData
        notifyDataSetChanged()
    }

    private class ViewHolder(
        val subject: TextView,
        val grade: TextView,
        val instructor: TextView,
        val semester: TextView
    )
}
