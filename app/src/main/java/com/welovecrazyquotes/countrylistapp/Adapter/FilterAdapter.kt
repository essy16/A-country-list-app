package com.welovecrazyquotes.countrylistapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.welovecrazyquotes.countrylistapp.databinding.FilterItemBinding
import com.welovecrazyquotes.countrylistapp.model.FilterString

class FilterAdapter(private val onItemClicked: (FilterString, isChecked:Boolean) -> Unit) :
    ListAdapter<FilterString, FilterAdapter.FilterViewHolder>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val filterViewHolder = FilterViewHolder(
            FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        filterViewHolder.itemView.setOnClickListener {
            val position = filterViewHolder.bindingAdapterPosition

        }
        return filterViewHolder
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    inner class FilterViewHolder(private val binding: FilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filterString: FilterString) {
            binding.apply {
                continent.text = filterString.stringFilter
                checkBox.isChecked = filterString.selected
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        Toast.makeText(root.context, "$filterString", Toast.LENGTH_SHORT).show()
                    }
                    onItemClicked(filterString, isChecked)
                }
            }
        }
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<FilterString>() {
            override fun areItemsTheSame(oldItem: FilterString, newItem: FilterString): Boolean {
                return oldItem.stringFilter == newItem.stringFilter
            }

            override fun areContentsTheSame(oldItem: FilterString, newItem: FilterString): Boolean {
                return oldItem == newItem
            }
        }
    }


}