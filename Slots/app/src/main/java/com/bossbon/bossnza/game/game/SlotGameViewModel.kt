package com.bossbon.bossnza.game.game

import androidx.lifecycle.*
import com.bossbon.bossnza.game.library.l
import com.bossbon.bossnza.game.game.recyclerView.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SlotGameViewModel(private val game: Boolean) : ViewModel() {
    var audioLength: Int = 0
    private val repository = SlotGameRepository()
    var currentBid = 100L
    var position1: Int = 0
    var position2: Int = 0
    var position3: Int = 0
    private val _currentList = MutableLiveData<CurrentSlots>()
    val currentList: LiveData<CurrentSlots> = _currentList
    private val _spinState = MutableLiveData(false)
    val spinState: LiveData<Boolean> = _spinState

    init {
        viewModelScope.launch {
            _currentList.postValue(repository.generateList(game))
        }
    }

    fun changeSpinningValue(value: Boolean) {
        _spinState.postValue(value)
    }

    fun checkWin(): List<Int> {
        val slotList = mutableListOf<Item>()
        slotList.add(currentList.value!!.s1.last())
        slotList.add(currentList.value!!.s2.last())
        slotList.add(currentList.value!!.s3.last())
        val slotIds = mutableListOf<Int>()
        val amountOfSames = mutableListOf<Int>()
        slotList.forEach {
            slotIds.add(it.id)
        }
        repeat(5) {
            amountOfSames.add(Collections.frequency(slotIds, it + 1))
        }
        l(amountOfSames.toString(), "amount of sames")
        l(slotIds.toString(), "slots")
        return amountOfSames
    }

    fun changeItems() {
        viewModelScope.launch (Dispatchers.Default) {
            val slotList = mutableMapOf<Int, Item>()
            slotList[1] = currentList.value!!.s1.last()
            slotList[2] = currentList.value!!.s2.last()
            slotList[3] = currentList.value!!.s3.last()
            val slot1 = mutableListOf<Item>()
            val slot2 = mutableListOf<Item>()
            val slot3 = mutableListOf<Item>()

            slot1.add(slotList[1]!!)
            slot1.addAll(repository.generate(game,29))

            slot2.add(slotList[2]!!)
            slot2.addAll(repository.generate(game,39))

            slot3.add(slotList[3]!!)
            slot3.addAll(repository.generate(game,49))



            viewModelScope.launch {
                _currentList.postValue(
                    CurrentSlots(
                        s1 = slot1,
                        s2 = slot2,
                        s3 = slot3,
                    )
                )
            }
        }
    }
}


class SlotViewModelFactory(private val game: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SlotGameViewModel(game) as T
    }
}