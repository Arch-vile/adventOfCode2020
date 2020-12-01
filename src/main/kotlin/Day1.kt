import java.io.File

fun readFile(fileName: String) =
        File(fileName).readLines()

fun main(args: Array<String>) {
    var numbers = readFile("./src/main/resources/day1Input.txt")
            .map { it.toInt() }

    var pairs = numbers.map { first ->
        numbers.map { second ->
            numbers.map { third ->
                Triple(first, second, third)
            }
        }
    }.flatten().flatten()

    var first = pairs.map { Pair(it, it.first + it.second + it.third) }
            .filter { it.second == 2020 }
            .map { it.first }
            .map { it.first * it.second * it.third }
            .first()

    println(first)
}
