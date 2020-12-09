package day9

import readFile


fun pairCombinations(numbers: List<Long>): List<Pair<Long,Long>>  =
  numbers
    .map { first ->
      numbers.map { second ->
        Pair(first, second)
      }
    }.flatten()
    .filter { it.first !== it.second }

fun pairCombinationsSums(dropLast: List<Long>): List<Long> =
  pairCombinations(dropLast).map { it.first + it.second }

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day9Input.txt")
    .map { it.toLong() }

  println(
  input
    .windowed(26)
    .filter {
      val foo = pairCombinationsSums(it.dropLast(1))
      val test = it[25]
      !foo.contains(test)
    }
    .map { it[25] }
    .first())

}


