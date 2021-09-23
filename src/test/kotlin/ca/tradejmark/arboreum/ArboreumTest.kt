package ca.tradejmark.arboreum

import ca.tradejmark.arboreum.common.Branch
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineFlapdoodleRule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.litote.kmongo.coroutine.coroutine
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArboreumTest() {
    val rule = CoroutineFlapdoodleRule(Any::class, dbName = TEST_DB)

    lateinit var arboreum: Arboreum

    @BeforeAll
    fun setupTestData() = runBlocking {
        arboreum = Arboreum(rule.mongoClient.coroutine, TEST_DB)
        rule.mongoClient.coroutine.getDatabase(TEST_DB).run {
            createCollection(BRANCHES)
            getCollection<Arboreum.DBBranch>(BRANCHES).insertMany(listOf(
                Arboreum.DBBranch("Empty Test",
                "empty-test",
                "This branch is empty.",
                    listOf()
                ),
                Arboreum.DBBranch(
                    "No Subs Test",
                    "no-subs",
                    "This branch has no sub-branches.",
                    listOf()
                ),
                Arboreum.DBBranch(
                    "The Master Branch",
                    "master-branch",
                    "This branch rules them all.",
                    listOf("empty-test", "no-subs")
                )
            ))
            createCollection("empty-test")
            createCollection("no-subs")
            getCollection<Arboreum.DBLeaf>("no-subs").insertMany(listOf(
                Arboreum.DBLeaf(
                    "I'm an article",
                    "an-article",
                    "This article is an article.",
                    "This is the article."
                ),
                Arboreum.DBLeaf(
                    "I'm another article",
                    "another-article",
                    "This is another article.",
                    "This is that other article."
                )
            ))
            createCollection("master-branch")
            getCollection<Arboreum.DBLeaf>("master-branch").insertOne(
                Arboreum.DBLeaf(
                "I'm a master article",
                "master-article",
                "This article rules them all.",
                "I rule over lesser articles with an iron fist."
            ))
            Unit
        }
    }

    @Test
    fun testEmptyBranch() = runBlocking {
        val b = arboreum.getBranch("empty-test")
        assertEquals(0, b.branches.size)
    }

    @Test
    fun testNoSubBranch() = runBlocking {
        val b = arboreum.getBranch("no-subs")
        assertEquals(0, b.branches.size)
        assertEquals(2, b.leaves.size)
        assertTrue {
            b.leaves.any { it.slug == "an-article" } && b.leaves.any { it.slug == "another-article" }
        }
    }

    @Test
    fun testGetLeaf() = runBlocking {
        val leaf = arboreum.getLeaf("no-subs", "an-article")
        assertEquals("I'm an article", leaf.meta.title)
        assertEquals("an-article", leaf.meta.slug)
        assertEquals("This article is an article.", leaf.meta.description)
        assertEquals("This is the article.", leaf.body)
    }

    @Test
    fun testMasterBranch() = runBlocking {
        val master = arboreum.getBranch("master-branch")
        assertEquals(2, master.branches.size)
        assertEquals(1, master.leaves.size)
        assertEquals("The Master Branch", master.meta.name)
        assertEquals("master-branch", master.meta.slug)
        assertEquals("This branch rules them all.", master.meta.description)
        assertTrue {
            master.branches.any { it.slug == "empty-test" } && master.branches.any { it.slug == "no-subs" }
        }
        assertTrue {
            master.leaves.any { it.slug == "master-article" }
        }
    }

    companion object {
        const val TEST_DB = "test"
        const val BRANCHES = "_branches"
    }
}