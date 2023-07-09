package com.bossbon.bossnza.game.game.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.ItemSlotBinding

class Adapter : RecyclerView.Adapter<ViewHolder>() {
    var items = mutableListOf<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSlotBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}

class ViewHolder(private val binding: ItemSlotBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item) {
        binding.apply {
            setImageForElement(item.id, slotImage)
            setImageForElement(item.idTop, slotImageTop)
            setImageForElement(item.idBottom, slotImageBottom)
        }
    }

    private fun setImageForElement(id: Int, element: ImageView) {
        when (id) {
            1 -> element.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                R.drawable.slot_item_1,
                null))
            2 -> element.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                R.drawable.slot_item_2,
                null))
            3 -> element.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                R.drawable.slot_item_3,
                null))
            4 -> element.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                R.drawable.slot_item_4,
                null))
            5 -> element.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                R.drawable.slot_item_5,
                null))
        }
    }
}