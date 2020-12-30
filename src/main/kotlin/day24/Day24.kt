package day24

import readFile
import kotlin.math.round
import kotlin.math.sqrt

fun main(args: Array<String>) {
  var steps =
    readFile("./src/main/resources/day24Input.txt")
      .map {
        it
          .replace("se", "SE")
          .replace("ne","NE")
          .replace("sw","SW")
          .replace("nw","NW")
          .replace("e","EE")
          .replace("w","WW")
      }
      .map {
        it.windowed(2,2)
      }

  var endCoordinates = steps
    .map {
      it.fold( Pair(0.toDouble(), 0.toDouble()))
      {acc, next ->
        val offset = offsetAmount(next)
        Pair(acc.first + offset.first , acc.second + offset.second)
      }
    }
    .map { Pair(round(it.first*100).toInt(), round(it.second*100).toInt()) }

  // Part 1
  endCoordinates.groupBy { it }
    .filter { it.value.size % 2 == 1 }
    .count().let { println(it) }

}

fun offsetAmount(direction: String): Pair<Double,Double> {
  val long = sqrt(3.0)/2.0+0.5
  val short = 1.0

  return when (direction) {
    "EE" -> Pair(2.0 * short,0.0)
    "WW" -> Pair(-2.0 * short, 0.0)
    "NW" -> Pair(-1 * short,long)
    "NE" -> Pair(short, long)
    "SE" -> Pair(short,-1 * long)
    "SW" -> Pair(-1 * short, -1 * long)
      else -> throw Error("Unknown direction $direction")
  }
}
