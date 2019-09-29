package com.ayikz.minesweeper

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView

class PlayActivity : AppCompatActivity(), RecyclerViewAdapter.RecyclerViewItemListener {

    // TODO: Inject
    private lateinit var viewModel: PlayViewModel

    private var adapter: RecyclerViewAdapter? = null
    private var flagsTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        flagsTextView = findViewById(R.id.tv_flags_remaining)

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, PlayViewModelFactory())[PlayViewModel::class.java]
        viewModel.navigator = navigator
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = RecyclerViewAdapter(this, this, viewModel.board)
        val layoutManager =
            GridLayoutManager(this, PlayViewModel.cellCount, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onItemClick(cell: Cell) {
        viewModel.onCellClicked(cell) {
            adapter?.updateBoard(viewModel.board)
        }
    }

    override fun onItemLongClick(cell: Cell) {
        viewModel.onCellLongClicked(cell) {
            adapter?.updateBoard(viewModel.board)
            flagsTextView?.text = viewModel.getRemainingFlags().toString()
        }
    }

    private val navigator: PlayNavigator = object : PlayNavigator {
        override fun showYouWonAlertDialog() {
            showAlertDialog("You Won! ${viewModel.getHappyEmoticon()}",
                "Hooray! You won the game. Play again?",
                "Play Again",
                "Leave")
        }

        override fun showGameOverAlertDialog() {
            showAlertDialog("You Lost! ${viewModel.getAngryEmoticon()}",
                "Oops, looks like you stepped on a mine.",
                "Retry",
                "Leave")
        }
    }

    private fun showAlertDialog(title: String,
                                message: String,
                                positiveButtonText: String,
                                negativeButtonText: String) {
        return AlertDialog.Builder(this).setMessage(message).setTitle(title)
            .setPositiveButton(positiveButtonText) { _, _ ->
                viewModel.generateBoard()
                adapter?.updateBoard(viewModel.board)
                flagsTextView?.text = viewModel.getRemainingFlags().toString()
            }.setNegativeButton(negativeButtonText) { _, _ ->
                this.finish()
            }.setCancelable(false).create().show()
    }
}
