package ca.tradejmark.arboreum.common

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SerializationTest {
    @Test
    fun leafSerializationTest() {
        val leaf = Leaf(Leaf.MetaLeaf("title", "slug", "desc"), "body")
        val ser = Json.encodeToString(leaf)
        val deser = Json.decodeFromString<Leaf>(ser)
        assertEquals(leaf, deser)
    }

    @Test
    fun branchSerializationTest() {
        val branch = Branch(
            Branch.MetaBranch("name", "slug", "desc"),
            listOf(Leaf.MetaLeaf("title", "slug", "desc")),
            listOf(Branch.MetaBranch("branch2", "b2", "desc2"))
        )
        val ser = Json.encodeToString(branch)
        val deser = Json.decodeFromString<Branch>(ser)
        assertEquals(branch, deser)
        assertEquals(1, deser.leaves.size)
        assertEquals(1, deser.branches.size)
    }
}