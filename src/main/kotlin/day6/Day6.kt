package day6

import java.io.File


fun main(args: Array<String>) {
    println(
            File("./src/main/resources/day6Input.txt")
                    .readText()
                    .split("\n\n")
                    .map { countDifferentLetters(it) }
                    .sum())
}

private fun countDifferentLetters(test: String) =
        test
                .replace("""\s""".toRegex(), "")
                .toCharArray()
                .toSet()
                .size

