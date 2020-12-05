package day5

import readFile

fun main(args: Array<String>) {
    println(readFile("./src/main/resources/day5Input.txt")
            .map { getRowAndColumn(it) }
            .map { it.first * 8 + it.second }
            .maxOrNull())
}

fun getRowAndColumn(boardingPass: String): Pair<Int,Int> {
    val asBinary = boardingPass
            .replace('F','0')
            .replace('B','1')
            .replace('R','1')
            .replace('L','0')

    val rowBinary = asBinary.substring(0,7)
    val columnBinary = asBinary.substring(7)
    return Pair(asInt(rowBinary), asInt(columnBinary))
}

fun asInt(seating: String) =
  Integer.parseInt(seating, 2)