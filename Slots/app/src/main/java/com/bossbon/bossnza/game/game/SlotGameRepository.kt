package com.bossbon.bossnza.game.game

import com.bossbon.bossnza.game.game.recyclerView.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SlotGameRepository {
    suspend fun generateList(game: Boolean): CurrentSlots {
        return suspendCoroutine { result ->
            CoroutineScope(Dispatchers.Default).launch {
                result.resume(
                    CurrentSlots(
                    generate(game, 30),
                    generate(game, 40),
                    generate(game, 50),
                )
                )
            }
        }
    }

    suspend fun generate(game: Boolean, amount: Int): MutableList<Item> {
        return suspendCoroutine {
            CoroutineScope(Dispatchers.Default).launch {
                val list = mutableListOf<Item>()
                repeat(amount) {
                    list.add(Item())
                }
                it.resume(list)
            }
        }
    }
}