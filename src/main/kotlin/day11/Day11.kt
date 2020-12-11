package day11

import readFile

fun main(args: Array<String>) {

  val input = readFile("./src/main/resources/day11Input.txt")
  val width = input[0].length
  val height = input.size

  var lobby = input.joinToString ( "" ).toCharArray().toList()
  var newLobby  = lobby

  do {
    lobby = newLobby
    newLobby = lobby
      .mapIndexed { index, char ->
        if(char == 'L') {
          if (allSurroundingsEmpty(index,lobby, width, height)) {
            '#'
          } else {
            'L'
          }
        } else if(char == '#') {
          if(hasNeighbourgs(index,lobby,width,height)) {
            'L'
          } else {
            '#'
          }
        } else '.'
      }
  } while(!areEqual(lobby, newLobby))


  println(
  newLobby.filter { it == '#' }.count()
  )
}

fun areEqual(lobby: List<Char>, newLobby: List<Char>) =
  lobby.toCharArray() contentEquals newLobby.toCharArray()

fun hasNeighbourgs(index: Int, lobby: List<Char>, width: Int, height: Int): Boolean {
  val coordinates: List<Int> = getSurrounds(index, width, height)
  var count = coordinates
    .map {
      lobby[it] == '#'
    }.filter { it }
    .count()
  return count >= 4
}

fun allSurroundingsEmpty(index: Int, lobby: List<Char>, width: Int, height: Int): Boolean {
  val coordinates: List<Int> = getSurrounds(index, width, height)

  for( coord in coordinates) {
    if(lobby[coord] == '#')
      return false
  }
  return true
}

private fun getSurrounds(index: Int, width: Int, height: Int): List<Int> {
  val coordinates: List<Int> = listOfNotNull(
    relativeTo(index, width, height, -1, -1),
    relativeTo(index, width, height, -1, 0),
    relativeTo(index, width, height, -1, 1),
    relativeTo(index, width, height, 0, -1),
    relativeTo(index, width, height, 0, 1),
    relativeTo(index, width, height, 1, -1),
    relativeTo(index, width, height, 1, 0),
    relativeTo(index, width, height, 1, 1))
  return coordinates
}

fun relativeTo(index: Int, width: Int, height: Int, yOffset: Int, xOffset: Int): Int? {
  var y = index / width
  var x = index - y * width
  y += yOffset
  x += xOffset

  return if(x < 0 || x >= width || y < 0 || y >= height)
    null
  else
    x + y * width
}
