package com.ayikz.minesweeper.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.ayikz.minesweeper.App
import com.ayikz.minesweeper.Cell
import com.ayikz.minesweeper.R
import kotlinx.android.synthetic.main.activity_play.*
import javax.inject.Inject

class PlayActivity : AppCompatActivity(),
    RecyclerViewAdapter.RecyclerViewItemListener {

    @Inject
    lateinit var playViewModelFactory: PlayViewModelFactory
    private lateinit var viewModel: PlayViewModel

    private var adapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        (application as App).playComponent.inject(this)

        setupViewModel()
        setupRecyclerView()
        setFlags()
    }

    private fun setFlags() {
        flagsTextView.text = viewModel.getRemainingFlags().toString()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, playViewModelFactory)[PlayViewModel::class.java]
        viewModel.navigator = navigator
    }

    private fun setupRecyclerView() {
        adapter = RecyclerViewAdapter(this, this, viewModel.getBoardCells())
        val layoutManager =
            GridLayoutManager(this,
                viewModel.cellCount, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onItemClick(cell: Cell) {
        viewModel.onCellClicked(cell) {
            updateScreen()
        }
    }

    override fun onItemLongClick(cell: Cell) {
        viewModel.onCellLongClicked(cell) {
            updateScreen()
        }
    }

    private fun updateScreen(){
        adapter?.updateBoard(viewModel.getBoardCells())
        setFlags()
    }

    private val navigator: PlayNavigator = object :
        PlayNavigator {
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
                viewModel.refreshBoard()
                adapter?.updateBoard(viewModel.getBoardCells())
                setFlags()
            }.setNegativeButton(negativeButtonText) { _, _ ->
                this.finish()
            }.setCancelable(false).create().show()
    }
}
