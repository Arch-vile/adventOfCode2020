import java.io.File

fun readFile(fileName: String)  =
    File(fileName).readLines()



fun main(args: Array<String>) {
//    var numbers = listOf(1721, 979, 366, 299, 675, 1456)

    var numbers = readFile("./src/main/resources/day1Input.txt")
            .map { it.toInt() }

    var pairs = numbers.map { first ->
        numbers.map { second -> Pair(first, second) }
    }.flatten()

    var first = pairs.map { Pair(it, it.first + it.second) }
            .filter { it.second == 2020 }
            .map { it.first }
            .map { it.first * it.second }
            .first()

    println(first)
}
