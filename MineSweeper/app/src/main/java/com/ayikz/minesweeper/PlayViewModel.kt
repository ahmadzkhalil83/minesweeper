package com.ayikz.minesweeper

import android.arch.lifecycle.ViewModel
import kotlin.random.Random

class PlayViewModel : ViewModel() {
    companion object {
        const val cellCount = 10
        const val mineCount = 14
    }

    private val angryEmoticons = arrayOf("""¯\_(⊙︿⊙)_/¯""",
        """┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻""",
        """(╯°□°）╯︵ ┻━┻""",
        """(ノಠ ∩ಠ)ノ彡( \o°o)\""")
    private val happyEmoticons = arrayOf("""ヽ(´▽`)/""",
            """\(ᵔᵕᵔ)/""",
            """(•̀ᴗ•́)و ̑̑""",
            """♪♪ ヽ(ˇ∀ˇ )ゞ""")

    var navigator: PlayNavigator? = null
    // TODO: Inject
    lateinit var board: Board

    init {
        generateBoard()
    }

    fun generateBoard() {
        val coordinatorGenerator = CoordinatesGenerator()
        board = Board(cellCount, cellCount, mineCount, coordinatorGenerator)
    }

    fun onCellClicked(cell: Cell, performAction: () -> Unit) {
        try {
            board.tap(cell)
            performAction.invoke()
        } catch (ex: MineException) {
            handleLosing()
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
        return if(remaining < 0) 0 else remaining
    }
    fun getHappyEmoticon() = getRandomItemFromArray(happyEmoticons)
    fun getAngryEmoticon() = getRandomItemFromArray(angryEmoticons)

    private fun handleLosing() = navigator?.showGameOverAlertDialog()
    private fun handleWinning() = navigator?.showYouWonAlertDialog()
    private fun getRandomItemFromArray(array: Array<String>): String {
        val index = Random.nextInt(0, array.size)
        return array[index]
    }
}