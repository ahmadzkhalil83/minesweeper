package com.ayikz.minesweeper

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class PlayViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel() as T
    }
}