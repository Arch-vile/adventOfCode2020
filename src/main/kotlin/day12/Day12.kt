package day12

import readFile
import java.lang.Math.abs


fun main(args: Array<String>) {

  val regexp = """([A-Z])(\d*)""".toRegex()
  val input = readFile("./src/main/resources/day12Input.txt")
    .map { regexp.find(it)!!.groupValues }
    .map { Pair(it[1][0], it[2].toInt()) }

  var direction = 90
  var x = 0
  var y = 0

  fun move(dir: Pair<Char, Int>) {
    when (dir.first) {
      'E' -> x += dir.second
      'W' -> x -= dir.second
      'N' -> y += dir.second
      'S' -> y -= dir.second
    }
  }

  input.forEach {
    when (it.first) {
      'R' -> direction = (direction + it.second) % 360
      'L' -> direction = (direction - it.second + 360) % 360
      'F' -> move(Pair(asCompassPoint(direction), it.second))
      else -> move(it)
    }
  }

  println( abs(x) + abs(y))
}

fun asCompassPoint(bearing: Int): Char {
  return when (bearing) {
    0 -> 'N'
    90 -> 'E'
    180 -> 'S'
    270 -> 'W'
    else -> throw Error("Unhandled bearing $bearing")
  }

}

