package com.ayikz.minesweeper

import android.graphics.Point
import kotlin.random.Random

class CoordinatesGenerator{
    fun getRandomCoordinate(min: Int = 0, max: Int): Int {
        return Random.nextInt(min, max)
    }

    fun getRandomPointOnAxis(xAxis: Int, yAxis: Int): Point {
        val xCoordinate = getRandomCoordinate(max = xAxis)
        val yCoordinate = getRandomCoordinate(max = yAxis)
        return Point(xCoordinate, yCoordinate)
    }
}