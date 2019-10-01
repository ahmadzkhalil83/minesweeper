package com.ayikz.minesweeper.dependency

import com.ayikz.minesweeper.Board
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
    fun provideBoard(coordinatesGenerator: CoordinatesGenerator): Board {
        return Board(Board.cellCount, Board.cellCount, Board.mineCount, coordinatesGenerator)
    }

    @Provides
    fun providePlayViewModel(board: Board): PlayViewModelFactory {
        return PlayViewModelFactory(board)
    }
}