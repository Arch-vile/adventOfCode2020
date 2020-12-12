package day11

import readFile

fun straightLineOffsets(xOffset: Int, yOffset: Int): Sequence<Pair<Int, Int>> {
  var x = xOffset
  var y = yOffset

  return sequence {
    while (true) {
      yield(Pair(x, y))
      x += xOffset
      y += yOffset
    }
  }
}

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day11Input.txt")
  val width = input[0].length
  val height = input.size

  var lobby = input.joinToString("").toCharArray().toList()
  var newLobby = lobby

  do {
    lobby = newLobby
    newLobby = lobby
      .mapIndexed { index, char ->
        if (char == 'L') {
          if (allSurroundingsEmpty(index, lobby, width, height)) {
            '#'
          } else {
            'L'
          }
        } else if (char == '#') {
          if (hasNeighbourgs(index, lobby, width, height)) {
            'L'
          } else {
            '#'
          }
        } else '.'
      }
  } while (!areEqual(lobby, newLobby))


  println(
    newLobby.filter { it == '#' }.count()
  )
}

fun areEqual(lobby: List<Char>, newLobby: List<Char>) =
  lobby.toCharArray() contentEquals newLobby.toCharArray()

fun hasNeighbourgs(index: Int, lobby: List<Char>, width: Int, height: Int): Boolean {
  val lines: List<List<Int>> = getSurrounds(index, width, height)
  var count =
    lines.mapNotNull { line ->
      line
        .map { lobby[it] }
        .firstOrNull { it != '.' }
    }
      .filter { it == '#' }
      .count()

  return count >= 5
}

fun allSurroundingsEmpty(index: Int, lobby: List<Char>, width: Int, height: Int): Boolean {
  val lines: List<List<Int>> = getSurrounds(index, width, height)

  for (line in lines) {
    for (coord in line) {
      if(lobby[coord] == 'L')
        break

      if (lobby[coord] == '#')
        return false
    }
  }
  return true
}

private fun getSurrounds(index: Int, width: Int, height: Int): List<List<Int>> {
  return listOf(
    straightLineIndexes(index, width, height, -1, -1),
    straightLineIndexes(index, width, height, 0, -1),
    straightLineIndexes(index, width, height, 1, -1),
    straightLineIndexes(index, width, height, -1, 0),
    straightLineIndexes(index, width, height, 1, 0),
    straightLineIndexes(index, width, height, -1, 1),
    straightLineIndexes(index, width, height, 0, 1),
    straightLineIndexes(index, width, height, 1, 1)
  )
}

private fun straightLineIndexes(index: Int, width: Int, height: Int, xOffset: Int, yOffset: Int): List<Int> {
  return straightLineOffsets(xOffset, yOffset)
    .map {
      relativeTo(index, width, height, it.first, it.second)
    }.takeWhile { it != null }.toList() as List<Int>
}

fun relativeTo(index: Int, width: Int, height: Int, xOffset: Int, yOffset: Int): Int? {
  var y = index / width
  var x = index - y * width
  y += yOffset
  x += xOffset

  return if (x < 0 || x >= width || y < 0 || y >= height)
    null
  else
    x + y * width
}
