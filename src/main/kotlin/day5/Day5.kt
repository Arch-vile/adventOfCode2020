package day5

import readFile

fun main(args: Array<String>) {
    println(
            readFile("./src/main/resources/day5Input.txt")
                    .asSequence()
                    .map { getRowAndColumn(it) }
                    .map { it.first * 8 + it.second }
                    .sortedBy { it }
                    .windowed(2, 1)
                    .map { Pair(it, it[1] - it[0]) }
                    .filter { it.second != 1 }
                    .map { it.first[0] + 1 }
                    .first()
    )
}

fun getRowAndColumn(boardingPass: String): Pair<Int, Int> {
    val asBinary = boardingPass
            .replace("""F|L""".toRegex(), "0")
            .replace("""B|R""".toRegex(), "1")

    val rowBinary = asBinary.substring(0, 7)
    val columnBinary = asBinary.substring(7)
    return Pair(asInt(rowBinary), asInt(columnBinary))
}

fun asInt(seating: String) =
        Integer.parseInt(seating, 2)