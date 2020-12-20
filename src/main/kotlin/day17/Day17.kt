package day17

import readFile

data class Coordinates(val x: Int, val y: Int, val z: Int)
data class Cube(var state: Boolean) {
  fun stateSymbol(): Char {
    return if (state) '#' else '.'
  }
}

class World {

  // Let's make world enough to contain everything after 6 rounds of expansion
  // Also +2 to make sure we have neighbours for the edge pieces available.
  var data: List<List<List<Cube>>> = IntRange(0, 40)
    .map {
      IntRange(0, 40)
        .map {
          IntRange(0, 40)
            .map { Cube(false) }
        }
    }

  fun getCube(x: Int, y: Int, z: Int) = data[x][y][z]

  fun all(): Sequence<Pair<Coordinates,Cube>> {
    return sequence {
      for (x in 1 until data.size - 1) {
        for (y in 1 until data[0].size - 1) {
          for (z in 1 until data[0][0].size - 1) {
            yield(Pair(Coordinates(x, y, z), data[x][y][z]))
          }
        }
      }
    }
  }

  fun getNeighbours(x: Int, y: Int, z: Int): List<Cube> {
    return IntRange(-1, 1).flatMap { xOffset ->
      IntRange(-1, 1).flatMap { yOffset ->
        IntRange(-1, 1).map { zOffset ->
          if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
            null
          } else {
            getCube(x + xOffset, y + yOffset, z + zOffset)
          }
        }
      }
    }.filterNotNull()
  }

}

fun main(args: Array<String>) {
  val input = readFile("./src/main/resources/day17Input.txt")
    .map { it.toCharArray() }

  val world = World()
  val xOffset = 15
  val yOffset = 15
  val z = 15

  // Load the initial state from input
  input.forEachIndexed { y, line ->
    line.forEachIndexed { x, cubeState ->
      if (cubeState == '#') {
        world.getCube(x + xOffset, -y + yOffset, z).state = true
      }
    }
  }

  repeat(6) {
    val statesToSwitch = mutableListOf<Cube>()
    world.all().forEach {
      val (x, y, z) = it.first
      val cube = it.second
      val activeNeighbours =
        world.getNeighbours(x, y, z)
          .filter { it.state }

      if (cube.state) {
        if (!(activeNeighbours.size == 2 || activeNeighbours.size == 3)) {
          statesToSwitch.add(cube)
        }
      } else {
        if (activeNeighbours.size == 3) {
          statesToSwitch.add(cube)
        }
      }
    }
    statesToSwitch.forEach { it.state = !it.state }
  }

  println(
    world.all()
      .filter { it.second.state }
      .count())

  printWorld(world)
}

fun printWorld(world: World) {
  for (z in world.data[0][0].indices) {
    println("Z: $z")
    for (y in world.data[0].indices) {
      for (x in world.data.indices) {
        print(world.getCube(x, world.data[0].size - 1 - y, z).stateSymbol())
      }
      println("")
    }
    println("-------------------------------------------------")
  }
}
