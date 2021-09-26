package ca.tradejmark.arboreum.common

import kotlinx.serialization.Serializable

@Serializable
data class Leaf(val meta: MetaLeaf, val body: String) {
    @Serializable
    data class MetaLeaf(val title: String, val slug: String, val description: String)
}
