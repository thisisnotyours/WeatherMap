package com.thisisnotyours.weathermap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.weathermap.R
import com.thisisnotyours.weathermap.databinding.RecyclerWeekTimeListBinding
import com.thisisnotyours.weathermap.model.WeekListItems

class WeekListAdapter(val items: List<WeekListItems>,
                      private val clickListener: (weekList: WeekListItems)-> Unit): RecyclerView.Adapter<WeekListAdapter.WeekListViewHolder>() {

    class WeekListViewHolder(val binding: RecyclerWeekTimeListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_week_time_list, parent, false)
        val viewHolder = WeekListViewHolder(RecyclerWeekTimeListBinding.bind(view))
        view.setOnClickListener{
            Toast.makeText(parent.context, "click ${items[viewHolder.adapterPosition]}", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeekListViewHolder, position: Int) {
        holder.binding.week = items[position]
    }

    override fun getItemCount(): Int = items.size
}