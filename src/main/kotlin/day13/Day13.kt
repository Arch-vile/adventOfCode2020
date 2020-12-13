package day13

import readFile

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day13Input.txt")


  val timestamp = input[0].toLong()
  val schedules = input[1].split(",")
    .filter { it != "x" }
    .map { it.toInt() }

  println(timestamp)
  val busLine = schedules
    .map {
      Pair(it,it - timestamp % it)
    }
    .minByOrNull { it.second }!!

  println(busLine)
  println(busLine.first * busLine.second)

}
