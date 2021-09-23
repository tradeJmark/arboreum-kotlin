package ca.tradejmark.arboreum

import ca.tradejmark.arboreum.common.Branch
import ca.tradejmark.arboreum.common.Leaf
import ca.tradejmark.arboreum.common.MissingBranchException
import ca.tradejmark.arboreum.common.MissingLeafException
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class Arboreum internal constructor(conn: CoroutineClient, dbName: String) {
    constructor(connUrl: String, dbName: String): this(KMongo.createClient(connUrl).coroutine, dbName)
    private val db = conn.getDatabase(dbName)

    suspend fun getBranch(branchSlug: String): Branch {
        val branches = db.getCollection<DBBranch>(BRANCHES)
        val leaves = db.getCollection<DBLeaf>(branchSlug).find().toList().map { it.toMetaLeaf() }
        val sb = branches.findBranch(branchSlug)
        val subs = sb.branches
            .map { branches.findBranch(it).toMetaBranch() }
        return Branch(sb.toMetaBranch(), leaves, subs)
    }

    suspend fun getLeaf(branchSlug: String, leafSlug: String): Leaf {
        val branch = db.getCollection<DBLeaf>(branchSlug)
        return branch.findLeaf(leafSlug).toLeaf()
    }


    internal data class DBBranch(
        val name: String,
        val slug: String,
        val description: String,
        val branches: List<String>) {
        fun toMetaBranch(): Branch.MetaBranch = Branch.MetaBranch(name, slug, description)
    }
    internal data class DBLeaf(
        val title: String,
        val slug: String,
        val description: String,
        val body: String) {
        fun toMetaLeaf(): Leaf.MetaLeaf = Leaf.MetaLeaf(title, slug, description)
        fun toLeaf(): Leaf = Leaf(toMetaLeaf(), body)
    }

    private suspend fun CoroutineCollection<DBBranch>.findBranch(slug: String): DBBranch =
        findOne(DBBranch::slug eq slug) ?: throw MissingBranchException(slug)

    private suspend fun CoroutineCollection<DBLeaf>.findLeaf(slug: String): DBLeaf =
        findOne(DBLeaf::slug eq slug) ?: throw MissingLeafException(namespace.collectionName, slug)

    companion object {
        private const val BRANCHES = "_branches"
    }
}
