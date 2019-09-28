package com.ayikz.minesweeper

import android.graphics.Point
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CellTest {
    @Test
    fun `test new cell has no bomb`() {
        val cell = Cell()
        assertFalse(cell.hasMine)
    }

    @Test
    fun `test new cell with bomb has bomb`() {
        val cell = Cell(hasMine = true)
        assertTrue(cell.hasMine)
    }

    @Test
    fun `when no neighboring cells bombs has been specified, neighboringMines should be 0`() {
        val cell = Cell()
        assertThat(cell.neighboringMines, equalTo(0))
    }

    @Test
    fun `when 4 neighboring cells bombs has been specified, neighboringMines should be 4`() {
        val cell = Cell(neighboringMines = 4)
        assertThat(cell.neighboringMines, equalTo(4))
    }

    @Test
    fun `when cell is create, isOpen is false`() {
        val cell = Cell()
        assertFalse(cell.isOpen)
    }

    @Test
    fun `when cell is create, and no x or y coordinates specified, they are 0,0`() {
        val cell = Cell()
        assertThat(cell.coordinates, equalTo(Point(0, 0)))
    }
}


