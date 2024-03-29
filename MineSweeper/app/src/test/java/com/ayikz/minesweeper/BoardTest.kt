package com.ayikz.minesweeper

import android.graphics.Point
import com.ayikz.minesweeper.CellState.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BoardTest {

    private val coordinatorGenerator = CoordinatesGenerator()

    @Test
    fun `board with 10 by 10 dimensions will return 10 vertical cells and 10 horizontal cells`() {
        val board = Board(10, 10, coordinatesGenerator = coordinatorGenerator)
        assertThat(board.verticalCells, equalTo(10))
        assertThat(board.horizontalCells, equalTo(10))
    }

    @Test
    fun `board with 10 by 10 dimensions should have 100 cells`() {
        val board = Board(10, 10, coordinatesGenerator = coordinatorGenerator)
        val cells = board.cells.flatten()
        assertThat(cells.count(), equalTo(100))
    }

    @Test
    fun `board with 5 by 5 dimensions should have 25 cells`() {
        val board = Board(5, 5, coordinatesGenerator = coordinatorGenerator)
        val cells = board.cells.flatten()
        assertThat(cells.count(), equalTo(25))
    }

    @Test
    fun `if board is given 5 mines, there should be 5 cells which have mines on the board`() {
        val board = Board(5, 5, 5, coordinatesGenerator = coordinatorGenerator)
        val mineCells = board.cells.flatten().filter { it.hasMine }
        assertThat(mineCells.count(), equalTo(5))
    }

    @Test
    fun `if board is given 10 mines, there should be 10 cells which have mines on the board`() {
        val board = Board(10, 10, 10, coordinatesGenerator = coordinatorGenerator)
        val mineCells = board.cells.flatten().filter { it.hasMine }
        assertThat(mineCells.count(), equalTo(10))

        for (cell in board.cells.flatten()) {
            print(cell.toString())
        }
    }

    @Test
    fun `when cell tapped on board, cell is opened`() {
        val board = Board(10, 10, 0, coordinatesGenerator = coordinatorGenerator)
        val cell = board.cellAt(1, 1)
        assertThat(cell.state, equalTo(CLOSED))
        board.tap(cell)
        assertThat(cell.state, equalTo(OPEN))
    }

    @Test
    fun `when cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(10, 10, 0, coordinatesGenerator = coordinatorGenerator)
        val cell = board.cellAt(1, 1)

        val neighboringCells = arrayListOf(board.cellAt(0, 0),
            board.cellAt(0, 1),
            board.cellAt(0, 2),
            board.cellAt(1, 0),
            board.cellAt(1, 2),
            board.cellAt(2, 0),
            board.cellAt(2, 1),
            board.cellAt(2, 2))

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(CLOSED))
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(OPEN))
        }
    }

    @Test
    fun `when top left corner cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(10, 10, 0, coordinatesGenerator = coordinatorGenerator)
        val cell = board.cellAt(0, 0)

        val neighboringCells =
            arrayListOf(board.cellAt(0, 1), board.cellAt(1, 0), board.cellAt(1, 1))

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(CLOSED))
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(OPEN))
        }
    }

    @Test
    fun `when bottom right corner cell tapped on board, and neighboring cells have no mines, open neighboring cells`() {
        val board = Board(100, 100, 0, coordinatesGenerator = coordinatorGenerator)
        val cell = board.cellAt(99, 99)

        val neighboringCells =
            arrayListOf(board.cellAt(99, 98), board.cellAt(98, 99), board.cellAt(98, 98))

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(CLOSED))
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(OPEN))
        }
    }

    @Test
    fun `when no mines on board, and cell is tapped, all cells will open`() {
        val board = Board(10, 10, 0, coordinatesGenerator = coordinatorGenerator)
        val cell = board.cellAt(5, 5)

        val neighboringCells = board.cells.flatten()

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(CLOSED))
        }

        board.tap(cell)

        for (neighboringCell in neighboringCells) {
            assertThat(neighboringCell.state, equalTo(OPEN))
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
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)

        board.tap(cell)

        val closedCells = board.cells.flatten().filter { it.state == CLOSED }
        assertThat(closedCells.count(), equalTo(1))
    }

    @Test
    fun `when cell is flagged on board, then cell state changes to FLAGGED and location added to flaggedLocations`() {
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)
        board.flagCell(cell)

        assertThat(cell.state, equalTo(FLAGGED))
        assertTrue(board.flaggedLocations.contains(Point(cell.coordinates.x, cell.coordinates.y)))
    }

    @Test
    fun `when flagged cell is tapped on board, then cell state changes to CLOSED`() {
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)
        board.flagCell(cell)
        assertThat(cell.state, equalTo(FLAGGED))
        board.tap(cell)
        assertThat(cell.state, equalTo(CLOSED))
    }

    @Test(expected = WonException::class)
    fun `when all mines have been flagged, board throws WonException`() {
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(2, 2)
        board.flagCell(cell)
    }

    @Test
    fun `when flagged cell is tapped on board, location is removed from flagged locations`() {
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)
        board.flagCell(cell)
        assertThat(cell.state, equalTo(FLAGGED))
        assertTrue(board.flaggedLocations.contains(Point(cell.coordinates.x, cell.coordinates.y)))
        board.tap(cell)
        assertFalse(board.flaggedLocations.contains(Point(cell.coordinates.x, cell.coordinates.y)))
    }

    @Test
    fun `when open cell is flagged, cell should remain in open state`() {
        val mockedCoordinatorGenerator: CoordinatesGenerator = mock()
        whenever(mockedCoordinatorGenerator.getRandomPointOnAxis(any(), any())).thenReturn(Point(2,
            2))
        val board = Board(5, 5, 1, mockedCoordinatorGenerator)
        val cell = board.cellAt(0, 0)
        board.tap(cell)
        assertThat(cell.state, equalTo(OPEN))
        board.flagCell(cell)
        assertThat(cell.state, equalTo(OPEN))
    }

    @Test
    fun `when board is refreshed, new cells mapping is generated`() {
        val board = Board(5, 5, 1, coordinatorGenerator)
        val cells = board.cells.flatten()
        board.refresh()
        val newCells = board.cells.flatten()
        var isCellsTheSame = true
        for (index in 0 until cells.count()) {
            if (cells[index].neighboringMines != newCells[index].neighboringMines ) {
                isCellsTheSame = false
            }
        }
        assertFalse(isCellsTheSame)
    }
}

