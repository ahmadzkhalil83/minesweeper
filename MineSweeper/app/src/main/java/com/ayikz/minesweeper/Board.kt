package com.ayikz.minesweeper

import android.graphics.Point
import android.support.annotation.VisibleForTesting
import com.ayikz.minesweeper.CellState.*

class Board(val verticalCells: Int,
            val horizontalCells: Int,
            private val numberOfMines: Int = 0,
            val coordinatesGenerator: CoordinatesGenerator) {

    var cells = arrayOf<Array<Cell>>()
    private var mineLocations = HashSet<Point>()
    @VisibleForTesting
    var flaggedLocations = HashSet<Point>()

    init {
        mineLocations = getMineLocations()
        generateBoard()
        scanForNeighboringMines()
    }

    fun cellAt(x: Int, y: Int) = cells[x][y]

    fun tap(cell: Cell) {
        if (cell.state == OPEN) return

        if (cell.state == FLAGGED) {
            flaggedLocations.remove(Point(cell.coordinates.x, cell.coordinates.y))
            cell.state = CLOSED
            return
        }

        if (cell.hasMine) throw MineException()

        cell.state = OPEN
        revealSafeArea(cell)
    }

    fun flagCell(cell: Cell) {
        if (cell.state == FLAGGED) return

        cell.state = FLAGGED
        flaggedLocations.add(Point(cell.coordinates.x, cell.coordinates.y))

        if (isAllMinesFlagged()) throw WonException()
    }

    private fun isAllMinesFlagged() : Boolean{
        if (flaggedLocations.count() == mineLocations.count()) {
            for (location in flaggedLocations) {
                if (!mineLocations.contains(location)) {
                    return false
                }
            }
            return true
        }
        return false
    }

    private fun revealSafeArea(cell: Cell) {
        if (cell.neighboringMines != 0) return

        val neighboringCells = getNeighboringCells(cell)

        while (neighboringCells.count() > 0) {
            val neighboringCell = neighboringCells.first()
            if (neighboringCell.state == OPEN) {
                neighboringCells.remove(neighboringCell)
                continue
            }

            neighboringCell.state = OPEN

            if (neighboringCell.neighboringMines == 0) {
                neighboringCells.addAll(getNeighboringCells(neighboringCell))
            }

            neighboringCells.remove(neighboringCell)
        }
    }

    private fun getNeighboringCells(cell: Cell): MutableList<Cell> {
        val neighbors = mutableListOf<Cell>()
        val x = cell.coordinates.x
        val y = cell.coordinates.y

        neighbors.apply {
            if (!isLeftBorderCell(x)) {
                add(cells[x - 1][y])
                // handle corner cells
                if (!isTopBorderCell(y)) add(cells[x - 1][y - 1])
                if (!isBottomBorderCell(y)) add(cells[x - 1][y + 1])
            }

            if (!isRightBorderCell(x)) {
                add(cells[x + 1][y])
                // handle corner cells
                if (!isTopBorderCell(y)) add(cells[x + 1][y - 1])
                if (!isBottomBorderCell(y)) add(cells[x + 1][y + 1])
            }

            if (!isTopBorderCell(y)) add(cells[x][y - 1])
            if (!isBottomBorderCell(y)) add(cells[x][y + 1])
        }

        return neighbors
    }

    private fun getMineLocations(): HashSet<Point> {
        val mines = HashSet<Point>()
        (0 until numberOfMines).forEach { _ ->
            var point: Point
            do {
                point = coordinatesGenerator.getRandomPointOnAxis(horizontalCells, verticalCells)
            } while (mines.contains(point))
            mines.add(point)
        }
        return mines
    }

    private fun generateBoard() {
        for (x in 0 until horizontalCells) {
            var horizontalCells = arrayOf<Cell>()
            for (y in 0 until verticalCells) {
                horizontalCells += createCell(x, y)
            }
            cells += horizontalCells
        }
    }

    private fun createCell(x: Int, y: Int): Cell {
        val cell = Cell(Point(x, y))
        cell.hasMine = mineLocations.contains(cell.coordinates)

        return cell
    }

    private fun scanForNeighboringMines() {
        for (cell in cells.flatten()) {
            cell.neighboringMines = getNeighboringMines(cell)
        }
    }

    private fun getNeighboringMines(cell: Cell): Int {
        var neighboringMines = 0
        val x = cell.coordinates.x
        val y = cell.coordinates.y

        if (hasLeftCellMine(x, y)) neighboringMines++
        if (hasRightCellMine(x, y)) neighboringMines++
        if (hasTopCellMine(x, y)) neighboringMines++
        if (hasBottomCellMine(x, y)) neighboringMines++
        if (hasTopLeftCellMine(x, y)) neighboringMines++
        if (hasBottomLeftCellMine(x, y)) neighboringMines++
        if (hasTopRightCellMine(x, y)) neighboringMines++
        if (hasBottomRightCellMine(x, y)) neighboringMines++

        return neighboringMines
    }

    private fun hasLeftCellMine(x: Int, y: Int) = (!isLeftBorderCell(x) && cells[x - 1][y].hasMine)
    private fun hasRightCellMine(x: Int, y: Int) = (!isRightBorderCell(x) && cells[x + 1][y].hasMine)
    private fun hasTopCellMine(x: Int, y: Int) = (!isTopBorderCell(y) && cells[x][y - 1].hasMine)
    private fun hasBottomCellMine(x: Int, y: Int) = (!isBottomBorderCell(y) && cells[x][y + 1].hasMine)
    private fun hasTopLeftCellMine(x: Int, y: Int) = (!(isTopBorderCell(y) || isLeftBorderCell(x)) && cells[x - 1][y - 1].hasMine)
    private fun hasBottomLeftCellMine(x: Int, y: Int) = (!(isBottomBorderCell(y) || isLeftBorderCell(x)) && cells[x - 1][y + 1].hasMine)
    private fun hasTopRightCellMine(x: Int, y: Int) = (!(isTopBorderCell(y) || isRightBorderCell(x)) && cells[x + 1][y - 1].hasMine)
    private fun hasBottomRightCellMine(x: Int, y: Int) = (!(isBottomBorderCell(y) || isRightBorderCell(x)) && cells[x + 1][y + 1].hasMine)

    private fun isLeftBorderCell(x: Int): Boolean = x - 1 < 0
    private fun isRightBorderCell(x: Int): Boolean = x + 1 >= horizontalCells
    private fun isTopBorderCell(y: Int): Boolean = y - 1 < 0
    private fun isBottomBorderCell(y: Int): Boolean = y + 1 >= verticalCells
}