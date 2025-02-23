package com.dev.ekart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.ekart.databinding.RowHomeBinding
import com.dev.ekart.pojo.Items

class HomeAdapter(val list: ArrayList<Items>,val listener:(item:Items) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding:RowHomeBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowHomeBinding.inflate(
                LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount()=list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item=list[position]
        holder.binding.root.setOnClickListener{ listener(list.get(position)) }
    }

    interface HomeRecyclerListener{
       fun onItemClick(item:Items)
    }
}
