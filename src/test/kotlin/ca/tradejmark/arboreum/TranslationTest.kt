package ca.tradejmark.arboreum

import ca.tradejmark.arboreum.common.Branch
import ca.tradejmark.arboreum.common.Leaf
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TranslationTest {
    @Test
    fun testMetaBranchTranslation() {
        val expected = Branch.MetaBranch("name", "slug", "desc")
        assertEquals(expected, DBB.toMetaBranch())
    }

    @Test
    fun testMetaLeafTranslation() {
        val expected = Leaf.MetaLeaf("title", "slug", "desc")
        assertEquals(expected, DBL.toMetaLeaf())
    }

    @Test
    fun testLeafTranslation() {
        val expected = Leaf(Leaf.MetaLeaf("title", "slug", "desc"), "body")
        assertEquals(expected, DBL.toLeaf())
    }

    companion object {
        internal val DBB = Arboreum.DBBranch("name", "slug", "desc", listOf("a", "b"))
        internal val DBL = Arboreum.DBLeaf("title", "slug", "desc", "body")
    }
}
