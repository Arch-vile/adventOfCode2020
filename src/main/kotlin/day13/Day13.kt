package day13

import readFile

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day13Input.txt")

  val timestamp = input[0].toLong()
  val schedules = input[1].split(",")
    .map { if (it == "x") "-1" else it }
    .map { it.toLong() }

  // Part 1
  val busLine = schedules
    .filter { it != -1L }
    .map {
      Pair(it, it - timestamp % it)
    }
    .minByOrNull { it.second }!!
  println(busLine.first * busLine.second)

  // Part 2
  // Magical values. Time is the solution to the:
  // 17,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,643,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,29,x,433,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x
  // and the step is the product of those 17*643*29*433
  var time = 124263591L
  val step = 137260567L
  while (true) {
    var match = true
    for (i in schedules.indices) {
      val schedule = schedules[i]
      if (schedule != -1L) {
          if ((time + i) % schedule != 0L) {
            match = false
            break
          }
      }
    }

    if (match) {
      println(time)
      break
    }
    time += step
  }

}
