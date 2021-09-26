package ca.tradejmark.arboreum.common

import kotlinx.serialization.Serializable

@Serializable
data class Branch(val meta: MetaBranch, val leaves: List<Leaf.MetaLeaf>, val branches: List<MetaBranch>) {
    @Serializable
    data class MetaBranch(val name: String, val slug: String, val description: String)
}
