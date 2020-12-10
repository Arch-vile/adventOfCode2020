package day10

import readFile


fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day10Input.txt")
    .map { it.toInt() }.toMutableList()

  input.add(0)
  input.sort()
  input.add(input.last()+3)

  // Part 1
  val differences = input.windowed(2,1)
    .map { it[1] - it[0] }
   val countByDifference = differences.groupBy { it.toString() }
  println(countByDifference.get("1")!!.size * countByDifference.get("3")!!.size)

  // Part 2


}