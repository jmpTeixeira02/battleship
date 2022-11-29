package isel.pdm.testutils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assert
import isel.pdm.ui.IsReadOnly

private fun isReadOnly(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(IsReadOnly)

fun SemanticsNodeInteraction.assertIsReadOnly(): SemanticsNodeInteraction =
    assert(isReadOnly())

fun SemanticsNodeInteraction.assertIsNotReadOnly(): SemanticsNodeInteraction =
    assert(!isReadOnly())

fun SemanticsNodeInteractionCollection.assertNotEmpty(): SemanticsNodeInteractionCollection {
    fetchSemanticsNodes(
        atLeastOneRootRequired = true,
        errorMessageOnFail = "Failed to assert not empty list of matching nodes"
    )
    return this
}

fun SemanticsNodeInteractionCollection.assertEmpty(): SemanticsNodeInteractionCollection {
    val matchedNodes = fetchSemanticsNodes(atLeastOneRootRequired = false)
    if (matchedNodes.isNotEmpty()) {
        throw AssertionError("Failed to assert empty list of matching nodes")
    }
    return this
}