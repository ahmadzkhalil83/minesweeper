package com.ayikz.minesweeper.dependency

import com.ayikz.minesweeper.ui.PlayActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlayModule::class, AppModule::class])
interface PlayComponent {
    fun inject(activity: PlayActivity)
}