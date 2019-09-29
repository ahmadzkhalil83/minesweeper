package com.ayikz.minesweeper

import android.app.Application
import com.ayikz.minesweeper.dependency.AppModule
import com.ayikz.minesweeper.dependency.DaggerPlayComponent
import com.ayikz.minesweeper.dependency.PlayComponent

class App: Application() {
    lateinit var playComponent: PlayComponent

    override fun onCreate() {
        super.onCreate()

        playComponent = DaggerPlayComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}