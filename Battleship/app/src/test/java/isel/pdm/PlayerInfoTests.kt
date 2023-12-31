package isel.pdm

import isel.pdm.game.lobby.model.PlayerInfo
import isel.pdm.game.lobby.model.validatePlayerUsername
import org.junit.Assert
import org.junit.Test

class PlayerInfoTests {

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank username throws`() {
        PlayerInfo(username = "\n  \t ")
    }

    @Test
    fun `create instance with non empty username`() {
        PlayerInfo(username = "username")
    }

    @Test
    fun `validatePlayerInfoParts returns false when username is blank`() {
        Assert.assertFalse(validatePlayerUsername("  \n"))
    }

    @Test
    fun `validatePlayerInfoParts returns false when username's length is bigger than 15 chars`() {
        Assert.assertFalse(validatePlayerUsername("abcdefghijklmnop"))
    }

    @Test
    fun `validatePlayerInfoParts returns true when username's length is between 1 and 15 chars`() {
        Assert.assertTrue(validatePlayerUsername("abcdefghijklmno"))
    }


}
