package day20

import java.io.File

data class EdgeState(val normal: Int, val reversed: Int, val isReversed: Boolean) {
  override fun toString(): String {
    return if (!isReversed) normal.toString() else reversed.toString()
  }

  fun value() = if (!isReversed) normal else reversed
}


class Tile(val tileNumber: Int, val mapData: List<String>) {

  val originalTop: Int
  var currentTop: EdgeState
  var currentBottom: EdgeState
  var currentLeft: EdgeState
  var currentRight: EdgeState

  init {
    val leftEdge = mapData.map { it.first() }.joinToString("")
    val rightEdge = mapData.map { it.last() }.joinToString("")
    currentTop = EdgeState(toId(mapData.first()), toId(mapData.first().reversed()), false)
    currentBottom = EdgeState(toId(mapData.last()), toId(mapData.last().reversed()), false)
    currentLeft = EdgeState(toId(leftEdge), toId(leftEdge.reversed()), false)
    currentRight = EdgeState(toId(rightEdge), toId(rightEdge.reversed()), false)
    originalTop = currentTop.value()
  }

  fun rotateUntil(predicate: (Tile) -> Boolean) {
    var counter = 0
    while (!predicate(this)) {
      this.rotateCW()
      counter++
      if (counter == 4)
        this.flipHorizontally()
    }
  }

  fun draw(): List<String> {
    val originalTopEdge =
      listOf(currentTop, currentBottom, currentRight, currentLeft)
        .first { it.normal == originalTop }

    val h = mapData.size - 1
    val w = mapData[0].length - 1
    var yOuter = false
    var xRange: IntProgression? = null
    var yRange: IntProgression? = null

    if (originalTopEdge == currentTop && !currentTop.isReversed) {
      yOuter = true
      yRange = 0..h
      xRange = 0..w
    }

    if (originalTopEdge == currentRight && !currentRight.isReversed) {
      yOuter = false
      xRange = 0..w
      yRange = h downTo 0
    }

    if (originalTopEdge == currentBottom && currentBottom.isReversed) {
      yOuter = true
      yRange = h downTo 0
      xRange = w downTo 0
    }

    if (originalTopEdge == currentLeft && currentLeft.isReversed) {
      yOuter = false
      xRange = w downTo 0
      yRange = 0..h
    }

    if (originalTopEdge == currentBottom && !currentBottom.isReversed) {
      yOuter = true
      yRange = h downTo 0
      xRange = 0..w
    }

    if (originalTopEdge == currentLeft && !currentLeft.isReversed) {
      yOuter = false
      xRange = 0..w
      yRange = 0..h
    }

    if (originalTopEdge == currentTop && currentTop.isReversed) {
      yOuter = true
      yRange = 0..h
      xRange = w downTo 0
    }

    if (originalTopEdge == currentRight && currentRight.isReversed) {
      yOuter = false
      xRange = w downTo 0
      yRange = h downTo 0
    }

    val dataRotated = if (yOuter) {
      yRange!!.map { y ->
        xRange!!.map { x -> mapData[y][x] }
      }
    } else {
      xRange!!.map { x ->
        yRange!!.map { y -> mapData[y][x] }
      }
    }.map { it.joinToString("") }

    return dataRotated
  }

  override fun toString(): String {
    return StringBuilder()
      .append(" ".plus(currentTop).plus(" ").padStart(12, '-').padEnd(21, '-'))
      .append("\n")
      .append("|".padEnd(20).plus("|"))
      .append("\n")
      .append("| ".plus(currentLeft).padEnd(8).plus(tileNumber).padEnd(16).plus(currentRight).plus(" |"))
      .append("\n")
      .append("|".padEnd(20).plus("|"))
      .append("\n")
      .append(" ".plus(currentBottom).plus(" ").padStart(12, '-').padEnd(21, '-'))
      .toString()
  }

  private fun toId(line: String): Int {
    return line
      .map { if (it == '#') 1 else 0 }
      .joinToString("")
      .toInt(2)
  }

  fun allEdgeCombinations(): List<Int> {
    return listOf(currentLeft, currentRight, currentBottom, currentTop)
      .flatMap { listOf(it.normal, it.reversed) }
  }

  // Any edge in any rotation
  fun hasEdge(edge: Int) = allEdgeCombinations().contains(edge)

  fun rotateCW() {
    val origLeft = currentLeft
    currentLeft = currentBottom
    currentBottom = currentRight.copy(isReversed = !currentRight.isReversed)
    currentRight = currentTop
    currentTop = origLeft.copy(isReversed = !origLeft.isReversed)
  }

  fun flipHorizontally() {
    val origTop = currentTop
    currentTop = currentBottom
    currentBottom = origTop
    currentRight = currentRight.copy(isReversed = !currentRight.isReversed)
    currentLeft = currentLeft.copy(isReversed = !currentLeft.isReversed)
  }
}

fun rotateCW(data: List<String>): List<String> {
  return utils.rotateCW(data.map { it.toCharArray().toList() })
    .map { it.joinToString("") }
}

fun flip(data: List<List<Char>>) = data.reversed()

fun main(args: Array<String>) {
  val input = File("./src/main/resources/day20Input.txt")
    .readText()
    .split("\n\n")
    .filter { it.isNotBlank() }
    .map {
      it.split("\n")
    }

  val tiles = input
    .map {
      Tile(getTileNumber(it), it.drop(1))
    }

  // Let's collect all the possible edge numbers and then group those by tiles and some other
  // magic and in the end we have 4 left
  // Part 1
  val corners = tiles
    .flatMap { tile ->
      tile.allEdgeCombinations().toSet()
        .map {
          Pair(tile.tileNumber, it)
        }
    }
    .groupBy { it.second }
    .values
    .filter { it.size == 1 }
    .groupBy { it[0].first }
    .values
    .filter { it.size > 3 }
    .map {
      it[0][0].first.toLong()
    }
  println(corners.reduce { acc, l -> acc * l })

  // Part 2
  val topLeftCorner = tiles.filter { it.tileNumber == corners[0].toInt() }.first()!!
  // Rotate it until it really is the top left corner
  topLeftCorner.rotateUntil { isTopLeft(it, tiles) }

  // Find the first tiles of each row
  val firstTiles = generateSequence(topLeftCorner) { previous ->
    val nextTile = tiles
      .filter { it.tileNumber !== previous.tileNumber }
      .firstOrNull { it.allEdgeCombinations().contains(previous.currentBottom.value()) }
    nextTile?.let {
      nextTile.rotateUntil { it.currentTop.value() == previous.currentBottom.value() }
    }
    nextTile
  }

  // Find the rest of the row for each first tile
  val worldMap = firstTiles.map {
    generateSequence(it) { previous ->
      val nextTile = tiles
        .filter { it.tileNumber !== previous.tileNumber }
        .firstOrNull { it.allEdgeCombinations().contains(previous.currentRight.value()) }
      nextTile?.let {
        nextTile.rotateUntil { it.currentLeft.value() == previous.currentRight.value() }
      }
      nextTile
    }.toList()
  }.toList()

  val trimmedmap = worldMap
    .map { tileRow ->
      tileRow.map { tile ->
        tile.draw().drop(1).dropLast(1)
          .map { it.drop(1).dropLast(1) }
      }
    }

  var combinedMap = trimmedmap
    .flatMap { tileRow ->
      IntRange(0, tileRow[0].size-1)
        .map { rowIndex ->
          tileRow.map { tile ->
            tile[rowIndex]
          }.joinToString("")
        }.map { it.toCharArray().toList() }
    }

  IntRange(1,7)
    .map { runIndex ->
      val monsterCount = findMonsters(combinedMap)
      var waves  = countWaves(combinedMap)
      println(waves - monsterCount * 15)
      if(runIndex == 4) {
        combinedMap = flip(combinedMap)
      }
      else {
        combinedMap = utils.rotateCW(combinedMap)
      }
      monsterCount
    }

}

private fun countWaves(combinedMap: List<List<Char>>): Int {
  return combinedMap
    .map {
      it.filter { char -> char == '#' }
    }
    .map { it.size  }
    .sum()
}

private fun findMonsters(combinedMap: List<List<Char>>): Int {
  var searchBoundary =
    (0..combinedMap.size - 3)
      .flatMap { y ->
        (0..combinedMap[0].size - 20)
          .map { x ->
            (0 until 3)
              .map { yOffset ->
                (0 until 20)
                  .map { xOffset ->
                    combinedMap[y + yOffset][x + xOffset]
                  }.joinToString("")
              }
          }
      }

  val monsterCount = searchBoundary
    .filter { hasMonster(it) }
    .count()

  return monsterCount
}

fun hasMonster(searchBoundary: List<String>): Boolean {
  var firstRow = searchBoundary[0].let {
    listOf(it[18])
  }
  var secondRow = searchBoundary[1].let {
    listOf(it[0],it[5],it[6],it[11],it[12],it[17],it[18],it[19])
  }
  var thirdRow = searchBoundary[2].let {
    listOf(it[1],it[4],it[7],it[10],it[13],it[16])
  }

  val success=  firstRow.toMutableSet().plus('#').plus(secondRow).plus(thirdRow).size == 1
  return success
}

fun drawMap(map: List<List<Tile>>) {
  map
    .map { tileList ->
      tileList.map { tile ->
        var foo = tile.draw()
        foo
      }
    }
    .forEach {
      it[0].indices
        .forEach { dataRowIndex ->
          it.forEach { tile ->
            print(tile[dataRowIndex] + "|")
          }
          println()
        }
      println()
    }
}

fun isTopLeft(tile: Tile, tiles: List<Tile>): Boolean {
  val right = tile.currentRight.value()
  val bottom = tile.currentBottom.value()

  val tileOnRight = tiles
    .filter { it.hasEdge(right) }
    .firstOrNull { it.tileNumber != tile.tileNumber }

  val tileOnBottom = tiles
    .filter { it.hasEdge(bottom) }
    .firstOrNull { it.tileNumber != tile.tileNumber }

  return tileOnRight != null && tileOnBottom != null
}


private fun getTileNumber(it: List<String>) =
  """Tile (\d+):""".toRegex().find(it.first())!!.groupValues[1].toInt()