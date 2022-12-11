package isel.pdm

import android.app.Application
import isel.pdm.preferences.PlayerInfoRepositorySharedPrefs
import isel.pdm.preferences.PlayerRepository


interface DependenciesContainer {
    val playerRepo: PlayerRepository
}

class BattleshipApplication : DependenciesContainer, Application() {

    override val playerRepo: PlayerRepository
        get() = PlayerInfoRepositorySharedPrefs(this)

}