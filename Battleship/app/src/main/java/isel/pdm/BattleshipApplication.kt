package isel.pdm

import android.app.Application
import isel.pdm.data.player.PlayerInfoRepositorySharedPrefs
import isel.pdm.data.player.PlayerRepository


interface DependenciesContainer {
    val playerRepo: PlayerRepository
}

class BattleshipApplication : DependenciesContainer, Application() {

    override val playerRepo: PlayerRepository
        get() = PlayerInfoRepositorySharedPrefs(this)

}