package com.ayikz.minesweeper

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BoardTest {

    private val coordinatorGenerator = CoordinatorGenerator()

    @Test
    fun `board with 10 by 10 dimensions will return 10 vertical cells and 10 horizontal cells`() {
        val board = Board(10, 10, coordinatorGenerator = coordinatorGenerator)
        assertThat(board.verticalCells, equalTo(10))
        assertThat(board.horizontalCells, equalTo(10))
    }

    @Test
    fun `board with 10 by 10 dimensions should have 100 cells`() {
        val board = Board(10, 10, coordinatorGenerator = coordinatorGenerator)
        val cells = board.cells.flatten()
        assertThat(cells.count(), equalTo(100))
    }

    @Test
    fun `board with 5 by 5 dimensions should have 25 cells`() {
        val board = Board(5, 5, coordinatorGenerator = coordinatorGenerator)
        val cells = board.cells.flatten()
        assertThat(cells.count(), equalTo(25))
    }

    @Test
    fun `if board is given 5 mines, there should be 5 cells which have mines on the board`() {
        val board = Board(5, 5, 5, coordinatorGenerator = coordinatorGenerator)
        val mineCells = board.cells.flatten().filter { it.hasMine }
        assertThat(mineCells.count(), equalTo(5))
    }

    @Test
    fun `if board is given 10 mines, there should be 10 cells which have mines on the board`() {
        val board = Board(10, 10, 10, coordinatorGenerator = coordinatorGenerator)
        val mineCells = board.cells.flatten().filter { it.hasMine }
        assertThat(mineCells.count(), equalTo(10))

        for (cell in board.cells.flatten()) {
            print(cell.toString())
        }
    }

    @Test
    fun `when cell tapped on board, cell is opened`() {
        val board = Board(10, 10, 0, coordinatorGenerator = coordinatorGenerator)
        val cell = board.cellAt(1, 1)
        assertFalse(cell.isOpen)
        board.tap(cell)
        assertTrue(cell.isOpen)
    }

    @Test
    fun `when cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(10, 10, 0, coordinatorGenerator = coordinatorGenerator)
        val cell = board.cellAt(1, 1)

        val neighboringCells = arrayListOf(
            board.cellAt(0, 0),
            board.cellAt(0, 1),
            board.cellAt(0, 2),
            board.cellAt(1, 0),
            board.cellAt(1, 2),
            board.cellAt(2, 0),
            board.cellAt(2, 1),
            board.cellAt(2, 2)
        )

        for (neighboringCell in neighboringCells) {
            assertFalse(neighboringCell.isOpen)
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertTrue(neighboringCell.isOpen)
        }
    }

    @Test
    fun `when top left corner cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(10, 10, 0, coordinatorGenerator = coordinatorGenerator)
        val cell = board.cellAt(0, 0)

        val neighboringCells = arrayListOf(
            board.cellAt(0, 1),
            board.cellAt(1, 0),
            board.cellAt(1, 1)
        )

        for (neighboringCell in neighboringCells) {
            assertFalse(neighboringCell.isOpen)
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertTrue(neighboringCell.isOpen)
        }
    }

    @Test
    fun `when bottom right corner cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(100, 100, 0, coordinatorGenerator = coordinatorGenerator)
        val cell = board.cellAt(99, 99)

        val neighboringCells = arrayListOf(
            board.cellAt(99, 98),
            board.cellAt(98, 99),
            board.cellAt(98, 98)
        )

        for (neighboringCell in neighboringCells) {
            assertFalse(neighboringCell.isOpen)
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertTrue(neighboringCell.isOpen)
        }
    }

    @Test
    fun `when no mines on board, and cell is tapped, all cells will open`() {
        val board = Board(10, 10, 0, coordinatorGenerator = coordinatorGenerator)
        val cell = board.cellAt(5, 5)

        val neighboringCells = board.cells.flatten()

        for (neighboringCell in neighboringCells) {
            assertFalse(neighboringCell.isOpen)
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertTrue(neighboringCell.isOpen)
        }
    }

    @Test(expected = MineException::class)
    fun `when click on cell with mine, throw mine exception`() {
        val board = Board(10, 10, 0, coordinatorGenerator)
        val cell = board.cellAt(5, 5)
        cell.hasMine = true
        board.tap(cell)
    }

    @Test
    fun `when there are mines on board, and cell is tapped, only safe cells will open`() {
        val mockedCoordinatorGenerator: CoordinatorGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomCoordinate(any(), any())).thenReturn(2)
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)

        board.tap(cell)

        val closedCells = board.cells.flatten().filter { !it.isOpen }
        assertThat(closedCells.count(), equalTo(9))
    }
}

