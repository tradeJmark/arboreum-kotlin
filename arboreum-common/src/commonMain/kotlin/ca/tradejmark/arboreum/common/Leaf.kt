package ca.tradejmark.arboreum.common

data class Leaf(val meta: MetaLeaf, val body: String) {
    data class MetaLeaf(val title: String, val slug: String, val description: String)
}
