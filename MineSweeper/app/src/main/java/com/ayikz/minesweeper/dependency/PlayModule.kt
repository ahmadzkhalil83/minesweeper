package com.ayikz.minesweeper.dependency

import com.ayikz.minesweeper.BoardManager
import com.ayikz.minesweeper.CoordinatesGenerator
import com.ayikz.minesweeper.ui.PlayViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PlayModule {

    @Provides
    fun provideCoordinatesGenerator(): CoordinatesGenerator {
        return CoordinatesGenerator()
    }

    @Provides
    fun provideBoardManager(coordinatesGenerator: CoordinatesGenerator): BoardManager {
        return BoardManager(coordinatesGenerator)
    }

    @Provides
    fun providePlayViewModel(boardManager: BoardManager): PlayViewModelFactory {
        return PlayViewModelFactory(boardManager)
    }
}