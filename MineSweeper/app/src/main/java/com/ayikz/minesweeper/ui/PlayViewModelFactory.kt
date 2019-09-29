package com.ayikz.minesweeper.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ayikz.minesweeper.BoardManager

class PlayViewModelFactory(private val boardManager: BoardManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel(boardManager) as T
    }
}