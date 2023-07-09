package com.bossbon.bossnza.game.lottery

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LotteryRepository {
    suspend fun generate(): MutableList<LotteryItem> {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.Default).launch {
                val indexes = mutableListOf<Int>()
                repeat(9) {
                    indexes.add(it)
                }
                val items = mutableListOf<LotteryItem>(
                    LotteryItem(itemValue = 2),
                    LotteryItem(itemValue = 2),
                    LotteryItem(itemValue = 2),
                    LotteryItem(itemValue = 3),
                    LotteryItem(itemValue = 3),
                    LotteryItem(itemValue = 3),
                    LotteryItem(itemValue = 5),
                    LotteryItem(itemValue = 5),
                    LotteryItem(itemValue = 5),
                )
                val listToReturn = mutableListOf<LotteryItem>()
                repeat(9) {
                    val randomIndex = indexes.random()
                    listToReturn.add(items[randomIndex])
                    indexes.remove(randomIndex)
                }
                continuation.resume(listToReturn)
            }
        }
    }
}