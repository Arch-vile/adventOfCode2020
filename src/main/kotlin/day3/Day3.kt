package day3

import readFile

data class Terrain(val pattern: List<List<Char>>) {

    fun isThereATree(location: Coordinate): Boolean =
        pattern[location.y][location.x % pattern[0].size] == '#'

    fun isThisTheBottom(location: Coordinate): Boolean =
        location.y + 1 == pattern.size
}

data class Coordinate(var x: Int, var y: Int) {
    fun add(xOffset: Int, yOffset: Int): Coordinate  =
        Coordinate(x + xOffset, y + yOffset)
}

typealias SlopeFunction = (Coordinate) -> Coordinate

data class Tobogga(val slope: SlopeFunction, var location: Coordinate) {
    fun move() {
        location = slope(location)
    }
}

fun main(args: Array<String>) {
    val terrain = Terrain(loadTerrainFromFile())

    val toboggas =  listOf(
            createTobogga { current -> current.add( 1, 1) },
            createTobogga { current -> current.add( 3,  1) },
            createTobogga { current -> current.add( 5,  1) },
            createTobogga { current -> current.add( 7,  1) },
            createTobogga { current -> current.add( 1,  2) })

    var totalTreeCount = toboggas.map {
        countTreesOnPath(terrain, it)
    }.reduce { acc, i -> acc * i }

    println(totalTreeCount)
}

private fun countTreesOnPath(terrain: Terrain, it: Tobogga): Int {
    var treeCount = 0
    while (!terrain.isThisTheBottom(it.location)) {
        it.move()
        if (terrain.isThereATree(it.location)) {
            treeCount++
        }
    }
    return treeCount
}

private fun createTobogga(slope: SlopeFunction): Tobogga {
    return Tobogga( slope, Coordinate(0, 0))
}

fun loadTerrainFromFile(): List<List<Char>> =
        readFile("./src/main/resources/day3Input.txt")
                .map { it.toCharArray().asList() }
