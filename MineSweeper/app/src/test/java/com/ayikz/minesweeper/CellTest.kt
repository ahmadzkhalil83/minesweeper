package com.ayikz.minesweeper

import android.graphics.Point
import com.ayikz.minesweeper.CellState.*
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
        assertThat(cell.state, equalTo(CLOSED))
    }

    @Test
    fun `when cell is create, and no x or y coordinates specified, they are 0,0`() {
        val cell = Cell()
        assertThat(cell.coordinates, equalTo(Point(0, 0)))
    }

    @Test
    fun `when cell coordinates is (0,0), and is closed, then background is light green`() {
        val cell = Cell(coordinates = Point(0,0))
        assertThat(cell.background(), equalTo(R.color.green_light))
    }

    @Test
    fun `when cell coordinates is (1,0), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(1,0))
        assertThat(cell.background(), equalTo(R.color.green_dark))
    }

    @Test
    fun `when cell coordinates is (2,0), and is closed, then background is light green`() {
        val cell = Cell(coordinates = Point(2,0))
        assertThat(cell.background(), equalTo(R.color.green_light))
    }

    @Test
    fun `when cell coordinates is (3,0), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(3,0))
        assertThat(cell.background(), equalTo(R.color.green_dark))
    }

    @Test
    fun `when cell coordinates is (0,1), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(0,1))
        assertThat(cell.background(), equalTo(R.color.green_dark))
    }

    @Test
    fun `when cell coordinates is (1,1), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(1,1))
        assertThat(cell.background(), equalTo(R.color.green_light))
    }

    @Test
    fun `when cell coordinates is (2,1), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(2,1))
        assertThat(cell.background(), equalTo(R.color.green_dark))
    }

    @Test
    fun `when cell coordinates is (3,1), and is closed, then background is dark green`() {
        val cell = Cell(coordinates = Point(3,1))
        assertThat(cell.background(), equalTo(R.color.green_light))
    }

    @Test
    fun `when cell coordinates is (0,0), and is open, then background is light gray`() {
        val cell = Cell(coordinates = Point(0,0), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_light))
    }

    @Test
    fun `when cell coordinates is (1,0), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(1,0), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_dark))
    }

    @Test
    fun `when cell coordinates is (2,0), and is open, then background is light gray`() {
        val cell = Cell(coordinates = Point(2,0), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_light))
    }

    @Test
    fun `when cell coordinates is (3,0), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(3,0), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_dark))
    }

    @Test
    fun `when cell coordinates is (0,1), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(0,1), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_dark))
    }

    @Test
    fun `when cell coordinates is (1,1), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(1,1), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_light))
    }

    @Test
    fun `when cell coordinates is (2,1), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(2,1), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_dark))
    }

    @Test
    fun `when cell coordinates is (3,1), and is open, then background is dark gray`() {
        val cell = Cell(coordinates = Point(3,1), state = OPEN)
        assertThat(cell.background(), equalTo(R.color.gray_light))
    }

    @Test
    fun `when cell is open and has neighboring mines, then cell text should be the number of neighboring mines`() {
        val cell = Cell(state = OPEN, neighboringMines = 2)
        assertThat(cell.getCellText(), equalTo(cell.neighboringMines.toString()))
    }

    @Test
    fun `when cell is flagged, then cell flag should be visible`() {
        val cell = Cell(state = FLAGGED)
        assertTrue(cell.isFlagVisible())
    }
}


