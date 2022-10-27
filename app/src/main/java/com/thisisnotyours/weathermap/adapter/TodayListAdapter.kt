package com.thisisnotyours.weathermap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.weathermap.R
import com.thisisnotyours.weathermap.databinding.RecyclerTodayTimeListBinding
import com.thisisnotyours.weathermap.model.TodayListItem

class TodayListAdapter(
    val items: List<TodayListItem>,
    private val clickListener: () -> Unit
): RecyclerView.Adapter<TodayListAdapter.TodayListViewHolder>(){

    class TodayListViewHolder(val binding: RecyclerTodayTimeListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_today_time_list, parent, false)
        val viewHolder = TodayListViewHolder(RecyclerTodayTimeListBinding.bind(view))
        view.setOnClickListener{
            Toast.makeText(parent.context, "click ${items[viewHolder.adapterPosition]}", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TodayListViewHolder, position: Int) {
        holder.binding.today = items[position]
    }

    override fun getItemCount(): Int = items.size
}