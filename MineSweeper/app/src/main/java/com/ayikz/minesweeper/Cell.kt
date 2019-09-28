package com.ayikz.minesweeper

import android.graphics.Point

data class Cell(
    val coordinates: Point = Point(0,0),
    var neighboringMines: Int = 0,
    var hasMine: Boolean = false,
    var isOpen: Boolean = false) {

    override fun toString(): String {
        return "\ncoordinates: (${coordinates.x},${coordinates.y}) - neighboringMines: $neighboringMines " +
                "- has Mines: $hasMine - isOpen: $isOpen"
    }
}