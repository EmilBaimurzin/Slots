package com.bossbon.bossnza.game.game

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bossbon.bossnza.game.game.recyclerView.Adapter

interface GameInterface {
    fun initAdapter(recyclerView: RecyclerView, context: Context): Adapter {
        val slotAdapter = Adapter()
        with(recyclerView) {
            adapter = slotAdapter
            val lm = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            lm.orientation = LinearLayoutManager.VERTICAL
            layoutManager = lm
            setHasFixedSize(true)
        }
        return slotAdapter
    }

    fun scrollListener(rv: RecyclerView, state: Boolean, callback: (RecyclerView) -> Unit, callbackScroll: (Int) -> Unit) {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!state) {
                    callback(recyclerView)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = lm.findLastVisibleItemPosition()
                callbackScroll.invoke(lastPosition)
            }
        })
    }

    fun smoothScroll(rv: RecyclerView, targetPosition: Int) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(rv.context) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return 100f / displayMetrics.densityDpi
                }
            }

        linearSmoothScroller.targetPosition = targetPosition
        rv.layoutManager!!.startSmoothScroll(linearSmoothScroller)
    }

    fun getValueOfIndex(index: Int): Int {
        return when (index) {
            0 -> 29
            1 -> 39
            2 -> 49
            else -> 0
        }
    }
}