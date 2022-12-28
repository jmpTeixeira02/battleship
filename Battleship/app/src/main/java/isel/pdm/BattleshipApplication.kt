package isel.pdm

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import isel.pdm.game.lobby.model.Lobby
import isel.pdm.game.lobby.model.LobbyFirebase
import isel.pdm.preferences.PlayerInfoRepositorySharedPrefs
import isel.pdm.preferences.PlayerRepository


interface DependenciesContainer {
    val playerRepo: PlayerRepository
    val lobby: Lobby
}

class BattleshipApplication : DependenciesContainer, Application() {

    override val playerRepo: PlayerRepository
        get() = PlayerInfoRepositorySharedPrefs(this)

    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }

    private val realFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override val lobby: Lobby
        get() = LobbyFirebase(realFirestoreDb)

//    override val match: Match
//        get() = MatchFirebase(emulatedFirestoreDb)
}