package com.jguzaa.bwell.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.databinding.ListItemHabitBinding

class HabitAdapter(val clickListener: HabitListener) : ListAdapter<Habit, HabitAdapter.ViewHolder>(HabitDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemHabitBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Habit, clickListener: HabitListener) {
            binding.habit = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemHabitBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HabitDiffCallback: DiffUtil.ItemCallback<Habit>(){
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.habitId == newItem.habitId
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }

}

class HabitListener(val clickListener: (habitId: Long) -> Unit) {
    fun onClick(habit: Habit) = clickListener(habit.habitId)
}