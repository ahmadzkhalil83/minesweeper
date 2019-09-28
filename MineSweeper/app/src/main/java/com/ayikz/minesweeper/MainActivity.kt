package com.ayikz.minesweeper

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RecyclerViewItemListener {
    companion object {
        private const val cellCount = 10
        private const val mineCount = 20
    }

    // TODO: Inject
    private lateinit var board: Board
    private var adapter: RecyclerViewAdapter? = null

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

    private fun showGameOverAlertDialog() {
        AlertDialog.Builder(this)
            .setMessage("""Oops, looks like you stepped on a mine ¯\_(⊙︿⊙)_/¯""")
            .setTitle("You Lost!")
            .setPositiveButton("Retry") { _, _ ->
                generateBoard()
                adapter?.updateBoard(this.board)
            }.setNegativeButton("Leave") { _, _ ->
                this.finish()
            }
            .setCancelable(false)
            .create()
            .show()
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
        board.flagCell(cell)
        adapter?.updateBoard(board)
    }
}
