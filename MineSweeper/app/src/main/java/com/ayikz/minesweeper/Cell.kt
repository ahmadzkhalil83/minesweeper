package com.ayikz.minesweeper

import android.graphics.Point
import com.ayikz.minesweeper.CellState.*

enum class CellState {
    CLOSED,
    OPEN,
    FLAGGED
}

data class Cell(val coordinates: Point = Point(0, 0),
                var neighboringMines: Int = 0,
                var hasMine: Boolean = false,
                var state: CellState = CLOSED) {


    override fun toString(): String {
        return "\ncoordinates: (${coordinates.x},${coordinates.y}) - neighboringMines: $neighboringMines "+
                "- has Mines: $hasMine - state: ${state.name}"
    }

    fun background() = if (state == OPEN) getOpenBackground() else getClosedBackground()

    private fun getOpenBackground(): Int {
        return if ((coordinates.x % 2 == 0 && coordinates.y % 2 == 0) || (coordinates.x % 2 != 0 && coordinates.y % 2 != 0)) {
            R.color.gray_light
        } else {
            R.color.gray_dark
        }
    }

    private fun getClosedBackground(): Int {
        return if ((coordinates.x % 2 == 0 && coordinates.y % 2 == 0) || (coordinates.x % 2 != 0 && coordinates.y % 2 != 0)) {
            R.color.green_light
        } else {
            R.color.green_dark
        }
    }

    fun getCellText(): String = if (state == OPEN && neighboringMines > 0) {
        neighboringMines.toString()
    } else {
        ""
    }

    fun isFlagVisible() = state == FLAGGED
}