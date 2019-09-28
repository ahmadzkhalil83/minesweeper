package com.ayikz.minesweeper

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RecyclerViewItemListener {
    companion object {
        private const val cellCount = 10
        private const val mineCount = 20
    }

    // TODO: Inject
    private lateinit var board: Board
    private var adapter: RecyclerViewAdapter? = null
    private val angryEmoticons = arrayOf("""¯\_(⊙︿⊙)_/¯""","""┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻""", """(╯°□°）╯︵ ┻━┻""","""(ノಠ ∩ಠ)ノ彡( \o°o)\""")
    private val happyEmoticons = arrayOf("""ヽ(´▽`)/""","""\(ᵔᵕᵔ)/""", """(•̀ᴗ•́)و ̑̑""","""♪♪ ヽ(ˇ∀ˇ )ゞ""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        generateBoard()
        adapter = RecyclerViewAdapter(this, this, board)
        val layoutManager = GridLayoutManager(this, cellCount, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    private fun generateBoard() {
        val coordinatorGenerator = CoordinatesGenerator()
        this.board = Board(cellCount, cellCount, mineCount, coordinatorGenerator)
    }

    private fun handleLosing() {
        showGameOverAlertDialog()
    }

    private fun handleWinning() {
        showYouWonAlertDialog()
    }

    private fun showYouWonAlertDialog() {
        createAlertDialog("You Won! ${getRandomItemFromArray(happyEmoticons)}",
            "Hooray! You won the game. Play again?",
            "Play Again",
            "Leave")
            .show()
    }

    private fun showGameOverAlertDialog() {
        createAlertDialog("You Lost! ${getRandomItemFromArray(angryEmoticons)}",
            "Oops, looks like you stepped on a mine.",
            "Retry",
            "Leave")
            .show()
    }

    private fun getRandomItemFromArray(array: Array<String>): String {
        val index = Random.nextInt(0, array.size)
        return array[index]
    }

    private fun createAlertDialog(title: String, message: String, positiveButtonText: String, negativeButtonText: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(positiveButtonText) { _, _ ->
                generateBoard()
                adapter?.updateBoard(this.board)
            }.setNegativeButton(negativeButtonText) { _, _ ->
                this.finish()
            }
            .setCancelable(false)
            .create()
    }

    override fun onItemClick(cell: Cell) {
        try {
            board.tap(cell)
            adapter?.updateBoard(board)
        } catch (ex: MineException) {
            handleLosing()
        }
    }

    override fun onItemLongClick(cell: Cell) {
        try {
            board.flagCell(cell)
            adapter?.updateBoard(board)
        } catch (e: WonException) {
            handleWinning()
        }
    }
}
