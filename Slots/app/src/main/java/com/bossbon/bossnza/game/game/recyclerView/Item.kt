package com.bossbon.bossnza.game.game.recyclerView

import com.bossbon.bossnza.game.library.random

data class Item (
    var id: Int = 1 random 5,
    var idTop: Int = 1 random 5,
    var idBottom: Int = 1 random 5
)