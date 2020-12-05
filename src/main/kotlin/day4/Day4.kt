package day4

import java.io.File

var matchers =
        mapOf(
                "byr" to """.*(byr):([^ ]*)""".toRegex(),
                "iyr" to """.*(iyr):([^ ]*)""".toRegex(),
                "eyr" to """.*(eyr):([^ ]*)""".toRegex(),
                "hgt" to """.*(hgt):([^ ]*)""".toRegex(),
                "hcl" to """.*(hcl):([^ ]*)""".toRegex(),
                "ecl" to """.*(ecl):([^ ]*)""".toRegex(),
                "pid" to """.*(pid):([^ ]*)""".toRegex(),
                "cid" to """.*(cid):([^ ]*)""".toRegex())

fun findValue(type: String, input: String) =
    matchers[type]?.find(input)?.groupValues?.get(2)

fun isValid(passport: String): Boolean {
    var requiredMatches = matchers.minus("cid")
    return requiredMatches
            .map { findValue(it.key, passport) }
            .filterNotNull()
            .count() == requiredMatches.size
}

fun main(args: Array<String>) {
 println( File("./src/main/resources/day4Input.txt")
         .readText()
         .split("\n\n")
         .map { isValid(it) }
         .filter { it }
         .count())
}



