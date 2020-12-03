package day3Part1

import readFile

data class Terrain(val pattern: List<List<Char>>) {

    fun isThereATree(x: Int, y: Int): Boolean {
        return pattern[y][x % pattern[0].size] == '#'
    }

    fun isThisTheBottom(y: Int): Boolean {
        return y + 1 == pattern.size
    }
}

data class Tobogga(var x: Int, var y: Int) {
    fun move() {
        x += 3
        y += 1
    }
}

fun main(args: Array<String>) {
    val terrain = Terrain(loadTerrainFromFile())
    val tobogga = Tobogga(0,0)

    var treeCount = 0
    while (!terrain.isThisTheBottom(tobogga.y)) {
       tobogga.move()
        if(terrain.isThereATree(tobogga.x, tobogga.y)) {
           treeCount++
        }
    }
    println(treeCount)
}

fun loadTerrainFromFile(): List<List<Char>> =
    readFile("./src/main/resources/day3Input.txt")
            .map { it.toCharArray().asList()}
