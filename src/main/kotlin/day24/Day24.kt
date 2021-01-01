package day24

import java.io.File

fun readFile(fileName: String) =
  File(fileName).readLines()

fun main(args: Array<String>) {
  var steps =
    readFile("./src/main/resources/day24Input.txt")
      .map {
        it
          .replace("se", "SE")
          .replace("ne", "NE")
          .replace("sw", "SW")
          .replace("nw", "NW")
          .replace("e", "EE")
          .replace("w", "WW")
      }
      .map {
        it.windowed(2, 2)
      }

  var endCoordinates = steps
    .map {
      it.fold(Pair(0, 0))
      { acc, next ->
        val offset = offsetAmount(next)
        Pair(acc.first + offset.first, acc.second + offset.second)
      }
    }
    .map { Pair(it.first, it.second) }

  val blackTilesMap = endCoordinates.groupBy { it }
    .filter { it.value.size % 2 == 1 }

  // Part 1
  blackTilesMap.count().let { println(it) }

  // Part 2
  part2(blackTilesMap.keys.toSet())
}

fun nextIn(direction: String, from: Pair<Int, Int>): Pair<Int, Int> {
  return offsetAmount(direction).let {
    Pair(from.first + it.first, from.second + it.second)
  }
}

private fun part2(blackTilesA: Set<Pair<Int, Int>>) {
  val loop = generateSequence(blackTilesA) { blackTiles ->
    val whiteTiles =
      blackTiles
        .flatMap { allNeighboursOf(it) }
        .filter { isNotABlackTile(it, blackTiles) }
        .toSet()

    val remainingBlack = blackTiles
      .filter {
        val neighbours = blackNeighboursOf(it, blackTiles)
        neighbours.size == 1 || neighbours.size == 2
      }.toSet()

    val whiteTurnedToBlack = whiteTiles
      .filter {
        blackNeighboursOf(it, blackTiles).size == 2
      }.toSet()

    val currentBlack = remainingBlack.plus(whiteTurnedToBlack)
    currentBlack.size.let { println(it) }
    currentBlack
  }

  loop.take(101).toList()
}

fun blackNeighboursOf(tile: Pair<Int, Int>, blackTiles: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
  return allNeighboursOf(tile)
    .filter { blackTiles.contains(it) }
    .toSet()
}

fun isNotABlackTile(tile: Pair<Int, Int>, blackTiles: Set<Pair<Int, Int>>) =
  !blackTiles.contains(tile)

fun allNeighboursOf(tile: Pair<Int, Int>): Set<Pair<Int, Int>> {
  return setOf("EE", "WW", "NW", "NE", "SE", "SW")
    .map { offsetAmount(it) }
    .map { Pair(it.first, it.second) }
    .map { Pair(tile.first + it.first, tile.second + it.second) }
    .toSet()
}

fun offsetAmount(direction: String): Pair<Int, Int> {
  val long = 1
  val short = 1

  return when (direction) {
    "EE" -> Pair(2 * short, 0)
    "WW" -> Pair(-2 * short, 0)
    "NW" -> Pair(-1 * short, long)
    "NE" -> Pair(short, long)
    "SE" -> Pair(short, -1 * long)
    "SW" -> Pair(-1 * short, -1 * long)
    else -> throw Error("Unknown direction $direction")
  }
}
