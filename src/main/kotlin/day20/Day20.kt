package day20

import java.io.File

class Tile(val tileNumber: Int, val mapData: List<String>) {

  val topEdgeId: Int
  val topEdgeIdReversed: Int
  val bottomEdgeId: Int
  val bottomEdgeIdReversed: Int
  val leftEdgeId: Int
  val leftEdgeIdReversed: Int
  val rightEdgeId: Int
  val rightEdgeIdReversed: Int


  init {
    topEdgeId = toId(mapData.first())
    topEdgeIdReversed = toId(mapData.first().reversed())
    bottomEdgeId = toId(mapData.last())
    bottomEdgeIdReversed = toId(mapData.last().reversed())
    val leftEdge = mapData.map { it.first() }.joinToString("")
    leftEdgeId = toId(leftEdge)
    leftEdgeIdReversed = toId(leftEdge.reversed())
    val rightEdge = mapData.map { it.last() }.joinToString("")
    rightEdgeId = toId(rightEdge)
    rightEdgeIdReversed = toId(rightEdge.reversed())
  }

  override fun toString(): String {
    return StringBuilder()
      .append(" ".plus(topEdgeId).plus(" ").padStart(12,'-').padEnd(21,'-'))
      .append("\n")
      .append("|".padEnd(20).plus("|"))
      .append("\n")
      .append("| ".plus(leftEdgeId).padEnd(8).plus(tileNumber).padEnd(16).plus(rightEdgeId).plus(" |"))
      .append("\n")
      .append("|".padEnd(20).plus("|"))
      .append("\n")
      .append(" ".plus(bottomEdgeId).plus(" ").padStart(12,'-').padEnd(21,'-'))
      .toString()
  }

  private fun toId(line: String): Int {
    return line
      .map { if(it == '#') 1 else 0 }
      .joinToString("")
      .toInt(2)
  }

}

fun main(args: Array<String>) {
  val input = File("./src/main/resources/day20Input.txt")
    .readText()
    .split("\n\n")
    .filter { it.isNotBlank() }
    .map {
      it.split("\n")
    }

   val tiles =  input
      .map {
        Tile(getTileNumber(it), it.drop(1))
      }

  // Let's collect all the possible edge numbers and then group those by tiles and some other
  // magic and in the end we have 4 left
  var foo  = tiles
    .flatMap {  tile ->
      listOf( tile.topEdgeId, tile.topEdgeIdReversed, tile.bottomEdgeId, tile.bottomEdgeIdReversed, tile.leftEdgeId, tile.leftEdgeIdReversed, tile.rightEdgeId, tile.rightEdgeIdReversed).toSet()
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
    .reduce { acc, l -> acc * l}

  println(foo)



}


private fun getTileNumber(it: List<String>) =
  """Tile (\d+):""".toRegex().find(it.first())!!.groupValues[1].toInt()