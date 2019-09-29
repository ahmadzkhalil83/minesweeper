package com.ayikz.minesweeper

class BoardManager(private val coordinatorGenerator: CoordinatesGenerator) {
    companion object {
        const val cellCount = 10
        const val mineCount = 15
    }

    fun generateBoard(): Board {
        return Board(cellCount, cellCount, mineCount, coordinatorGenerator)
    }
}