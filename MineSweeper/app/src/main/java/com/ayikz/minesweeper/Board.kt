package com.ayikz.minesweeper

import android.graphics.Point

class Board(val verticalCells: Int,
            val horizontalCells: Int,
            private val numberOfMines: Int = 0,
            val coordinatorGenerator: CoordinatorGenerator) {

    var cells = arrayOf<Array<Cell>>()

    init {
        generateBoard(getMineLocations())
        scanForNeighboringMines()
    }

    fun cellAt(x: Int, y: Int) = cells[x][y]

    fun tap(cell: Cell) {
        if (cell.isOpen) return
        if (cell.hasMine) throw MineException()

        cell.isOpen = true
        revealSafeArea(cell)
    }

    private fun revealSafeArea(cell: Cell) {
        if (cell.neighboringMines != 0) return

        val neighboringCells = getNeighboringCells(cell)

        while (neighboringCells.count() > 0) {
            val neighboringCell = neighboringCells.first()
            if (neighboringCell.isOpen) {
                neighboringCells.remove(neighboringCell)
                continue
            }

            if (neighboringCell.neighboringMines == 0) {
                neighboringCell.isOpen = true
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
            if (!isLeftBorderCell(x)) add(cells[x - 1][y])
            if (!isRightBorderCell(x)) add(cells[x + 1][y])
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
                point = getRandomMineCoordinate()
            } while (mines.contains(point))
            mines.add(point)
        }
        return mines
    }

    private fun getRandomMineCoordinate(): Point {
        val xCoordinate = coordinatorGenerator.getRandomCoordinate(max = horizontalCells)
        val yCoordinate = coordinatorGenerator.getRandomCoordinate(max = verticalCells)
        return Point(xCoordinate, yCoordinate)
    }

    private fun generateBoard(mineLocations: HashSet<Point>) {
        for (x in 0 until horizontalCells) {
            var horizontalCells = arrayOf<Cell>()
            for (y in 0 until verticalCells) {
                val cell = Cell(Point(x, y))
                cell.hasMine = mineLocations.contains(cell.coordinates)
                horizontalCells += cell
            }
            cells += horizontalCells
        }
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