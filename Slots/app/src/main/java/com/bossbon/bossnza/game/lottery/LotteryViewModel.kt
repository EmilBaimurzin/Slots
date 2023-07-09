package com.bossbon.bossnza.game.lottery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LotteryViewModel: ViewModel() {
    private val repository = LotteryRepository()
    private val _items = MutableLiveData<MutableList<LotteryItem>>(mutableListOf())
    val items: LiveData<MutableList<LotteryItem>> = _items

    private val _clickState = MutableLiveData<Boolean>(true)
    val clickState: LiveData<Boolean> = _clickState

    init {
        viewModelScope.launch {
            _items.postValue(repository.generate())
        }
    }

    fun openItem(index: Int) {
        val newList = _items.value
        newList!![index].isOpened = true
        _items.postValue(newList!!)
    }

    fun changeClickState(value: Boolean) {
        _clickState.postValue(value)
    }
}