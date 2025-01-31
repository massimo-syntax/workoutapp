package com.example.workoutapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.databinding.ItemExcerciseBinding

class ExerciseAdapter(val items : List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(binding : ItemExcerciseBinding) : RecyclerView.ViewHolder(binding.root){
        val itemNumber = binding.tvExcerciseNumberItem
        val itemImage = binding.ivExerciseItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        val _binding = ItemExcerciseBinding.inflate(layout, parent, false)
        val viewHolder : ViewHolder = ViewHolder(_binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // names defined in inner class ViewHolder..

        if(item.selected){
            holder.itemNumber.setBackgroundResource(R.drawable.round_filled_counter_center)
            holder.itemNumber.setTextColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemNumber.background = null
            holder.itemNumber.setTextColor( Color.parseColor("#FF018786") )
        }
        holder.itemNumber.text = (position+1).toString()
        holder.itemImage.setImageResource(item.image)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}