package day12

import readFile
import java.lang.Math.abs


fun main(args: Array<String>) {

  val regexp = """([A-Z])(\d*)""".toRegex()
  val input = readFile("./src/main/resources/day12Input.txt")
    .map { regexp.find(it)!!.groupValues }
    .map { Pair(it[1][0], it[2].toInt()) }

  var speedX = 10
  var speedY = 1
  var shipX = 0
  var shipY = 0

  fun rotate(dir: Pair<Char, Int>) {
    var angle = dir.second
    if (dir.first == 'L') {
      angle = -1 * angle + 360
    }

    repeat(angle / 90) {
      var oldSpeedX = speedX
      speedX = speedY
      speedY = -1 * oldSpeedX
    }
  }

  fun alterSpeed(dir: Pair<Char, Int>) {
    when (dir.first) {
      'E' -> speedX += dir.second
      'W' -> speedX -= dir.second
      'N' -> speedY += dir.second
      'S' -> speedY -= dir.second
      'L', 'R' -> rotate(dir)
    }
  }

  fun move(amount: Int) {
    shipX +=  speedX * amount
    shipY += speedY * amount
  }

  input.forEach {
    when (it.first) {
      'F' -> move(it.second)
      else -> alterSpeed(it)
    }
  }

  println(abs(shipX) + abs(shipY))
}


