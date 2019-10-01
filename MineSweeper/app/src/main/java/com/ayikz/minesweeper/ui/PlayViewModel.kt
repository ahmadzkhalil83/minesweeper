package com.ayikz.minesweeper.ui

import android.arch.lifecycle.ViewModel
import com.ayikz.minesweeper.*
import kotlin.random.Random

class PlayViewModel(private val board: Board) : ViewModel() {
    private val angryEmoticons = arrayOf("""¯\_(⊙︿⊙)_/¯""",
        """┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻""",
        """(╯°□°）╯︵ ┻━┻""",
        """(ノಠ ∩ಠ)ノ彡( \o°o)\""")
    private val happyEmoticons =
        arrayOf("""ヽ(´▽`)/""", """\(ᵔᵕᵔ)/""", """(•̀ᴗ•́)و ̑̑""", """♪♪ ヽ(ˇ∀ˇ )ゞ""")

    var navigator: PlayNavigator? = null
    var cellCount = Board.cellCount
    var mineCount = Board.mineCount

    fun getBoardCells(): List<Cell> {
        return board.cells.flatten()
    }

    fun onCellClicked(cell: Cell, performAction: () -> Unit) {
        try {
            board.tap(cell)
            performAction.invoke()
        } catch (ex: MineException) {
            handleLosing()
        } catch (e: WonException) {
            handleWinning()
        }
    }

    fun onCellLongClicked(cell: Cell, performAction: () -> Unit) {
        try {
            board.flagCell(cell)
            performAction.invoke()
        } catch (e: WonException) {
            handleWinning()
        }
    }

    fun getRemainingFlags(): Int {
        val remaining = mineCount - board.flaggedLocations.count()
        return if (remaining < 0) 0 else remaining
    }

    fun getHappyEmoticon() = getRandomItemFromArray(happyEmoticons)
    fun getAngryEmoticon() = getRandomItemFromArray(angryEmoticons)

    private fun handleLosing() = navigator?.showGameOverAlertDialog()
    private fun handleWinning() = navigator?.showYouWonAlertDialog()
    private fun getRandomItemFromArray(array: Array<String>): String {
        val index = Random.nextInt(0, array.size)
        return array[index]
    }

    fun refreshBoard() {
        board.refresh()
    }
}