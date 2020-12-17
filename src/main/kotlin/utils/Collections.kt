package utils


fun <T> subsets(collection: List<T>): List<List<T>> {
  val subsets = mutableListOf<List<T>>()
  val n: Int = collection.size
  for (i in 0 until (1 shl n)) {
    val subset = mutableListOf<T>()
    for (j in 0 until n) {
      if (i and (1 shl j) > 0) {
        subset.add(collection[j])
      }
    }
    subsets.add(subset)
  }
  return subsets.filter { it.isNotEmpty() }
}

