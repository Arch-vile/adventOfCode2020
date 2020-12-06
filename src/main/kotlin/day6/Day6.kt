package day6

import java.io.File

fun main(args: Array<String>) {
    val questionaires = File("./src/main/resources/day6Input.txt")
            .readText()
            .split("\n\n")

    // Part 1
    println(
            questionaires.map {
                it.replace("""\s""".toRegex(), "")
                        .toCharArray()
                        .toSet()
                        .size
            }.sum())

    // Part 2
    println(
            questionaires
                    .map { it.split("\n") }
                    .map {
                        it.map { personAnswers -> personAnswers.toCharArray().toSet() }
                                .reduce { acc, next -> acc.intersect(next) }
                                .count()
                    }.sum()
    )
}

