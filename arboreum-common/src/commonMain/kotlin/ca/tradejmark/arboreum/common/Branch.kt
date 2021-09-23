package ca.tradejmark.arboreum.common

data class Branch(val meta: MetaBranch, val leaves: List<Leaf.MetaLeaf>, val branches: List<MetaBranch>) {
    data class MetaBranch(val name: String, val slug: String, val description: String)
}
