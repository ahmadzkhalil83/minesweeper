package com.ayikz.minesweeper.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ayikz.minesweeper.Board

class PlayViewModelFactory(private val board: Board) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel(board) as T
    }
}