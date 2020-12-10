package day10

import readFile


fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day10Input.txt")
    .map { it.toLong() }.toMutableList()

  input.add(0)
  input.sort()
  input.add(input.last() + 3)

  // Part 1
  var differences = input.windowed(2, 1)
    .map { it[1] - it[0] }
  val countByDifference = differences.groupBy { it.toString() }
  println(countByDifference.get("1")!!.size * countByDifference.get("3")!!.size)

  // Part 2
  val parts: MutableList<List<Long>> = mutableListOf()
  while (differences.isNotEmpty()) {
    var part = differences.takeWhile { it != 3L }
    if (part.isNotEmpty())
      parts.add(part)
    differences = differences.drop(part.size + 1)
  }

  val product =
    parts
      .map { it.size }
      .filter { it != 0 }
      .map {
        when (it) {
          1 -> 1L
          2 -> 2L
          3 -> 4L
          else -> 7L
        }
      }
      .reduce { acc, next -> acc * next }
  println(product)
}