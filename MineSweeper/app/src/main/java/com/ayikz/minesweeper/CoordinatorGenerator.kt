package com.ayikz.minesweeper

import kotlin.random.Random

class CoordinatorGenerator{
    fun getRandomCoordinate(min: Int = 0, max: Int): Int {
        return Random.nextInt(min, max)
    }
}