package com.ayikz.minesweeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RecyclerViewItemListener {

    private var board: Board? = null
    private var adapter: RecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        board = createBoard()
        board?.let { board ->
            adapter = RecyclerViewAdapter(this, this, board)
            val layoutManager = GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
    }

    private fun createBoard(): Board{
        val coordinatorGenerator = CoordinatorGenerator()
        return Board(6,6,4, coordinatorGenerator)
    }

    override fun onItemClick(cell: Cell) {
        Log.i("TAG", "Clicked cell at x: ${cell.coordinates.x} - y: ${cell.coordinates.y}")
        board?.let { board ->
            try {
                board.tap(cell)
                adapter?.updateBoard(board)
            } catch (ex: MineException) {
                Toast.makeText(this, "YOU LOST!", Toast.LENGTH_LONG).show()
                this.board = createBoard()
                adapter?.updateBoard(this.board!!)
            }
        }

    }
}
