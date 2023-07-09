package com.bossbon.bossnza.game.lottery

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bossbon.bossnza.R
import com.bossbon.bossnza.databinding.ItemLotteryBinding

class LotteryAdapter(
    private val itemClickListener: (position: Int, item: LotteryItem) -> Unit,
    private val soundValue: Boolean
) : RecyclerView.Adapter<LotteryViewHolder>() {
    var items = mutableListOf<LotteryItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotteryViewHolder {
        return LotteryViewHolder(
            ItemLotteryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context, soundValue
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LotteryViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemClickListener = itemClickListener
    }
}

class LotteryViewHolder(
    private val binding: ItemLotteryBinding,
    private val context: Context,
    private val soundValue: Boolean
) :
    RecyclerView.ViewHolder(binding.root) {
    var itemClickListener: ((position: Int, item: LotteryItem) -> Unit)? = null
    fun bind(item: LotteryItem) {

        if (item.isOpened) {
            setImage(item.itemValue)
            binding.imgLottery.setOnClickListener {}
        } else {
            binding.imgLottery.setOnClickListener { view ->
                if (soundValue) {
                    MediaPlayer.create(context, R.raw.sound_erase).start()
                }
                view as ImageView
                setImage(item.itemValue)
                view.alpha = 0f
                view.animate()
                    .setDuration(500)
                    .alpha(1f)
                    .start()
                itemClickListener?.invoke(adapterPosition, item)
            }
        }

        if (item.isOpened.not()) {
            binding.imgLottery.setImageIcon(null)
        }
    }

    private fun setImage(itemValue: Int) {
        when (itemValue) {
            2 -> binding.imgLottery.setImageResource(R.drawable.img_2x)
            3 -> binding.imgLottery.setImageResource(R.drawable.img_3x)
            else -> binding.imgLottery.setImageResource(R.drawable.img_5x)
        }
    }
}