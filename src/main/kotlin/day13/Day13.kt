package day13

import readFile

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/testInput.txt")


  val timestamp = input[0].toLong()
  val schedules = input[1].split(",")
    .map { if(it == "x") "-1" else it }
    .map { it.toInt() }

  // Part 1
  val busLine = schedules
    .filter { it != -1 }
    .map {
      Pair(it,it - timestamp % it)
    }
    .minByOrNull { it.second }!!
  println(busLine.first * busLine.second)


  // Part 2
  var time = 1
  while(true) {
    var match = true
    for (i in 0 until schedules.size) {
      val schedule = schedules[i]
      if (schedule != -1) {
        var offSet = time % schedule
        if ((schedule - offSet) % schedule != i) {
          match = false
          break
        }
      }
    }

    if (match) {
      println(time)
      break
    }
    time++
  }

}
