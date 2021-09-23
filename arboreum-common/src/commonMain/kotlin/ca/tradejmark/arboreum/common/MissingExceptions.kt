package ca.tradejmark.arboreum.common

class MissingBranchException(slug: String): Exception("Branch with slug '$slug' could not be found.")
class MissingLeafException(branchSlug: String, leafSlug: String):
    Exception("Leaf $leafSlug from branch $branchSlug could not be found.")