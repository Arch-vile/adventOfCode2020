package day4

import day4.FieldType.byr
import day4.FieldType.cid
import day4.FieldType.ecl
import day4.FieldType.eyr
import day4.FieldType.hcl
import day4.FieldType.hgtCm
import day4.FieldType.hgtIn
import day4.FieldType.iyr
import day4.FieldType.pid
import java.io.File

enum class FieldType {
    byr, iyr, eyr, hgtCm, hgtIn, hcl, ecl, pid, cid,
}

var matchers =
        mapOf(
                byr to """.*(byr):(\d{4}) """.toRegex(),
                iyr to """.*(iyr):(\d{4}) """.toRegex(),
                eyr to """.*(eyr):(\d{4}) """.toRegex(),
                hgtCm to """.*(hgt):(\d*)cm """.toRegex(),
                hgtIn to """.*(hgt):(\d*)in """.toRegex(),
                hcl to """.*(hcl):(#[a-f0-9]{6}) """.toRegex(),
                ecl to """.*(ecl):(amb|blu|brn|gry|grn|hzl|oth) """.toRegex(),
                pid to """.*(pid):(\d{9}) """.toRegex(),
                cid to """.*(cid):([^ ]*) """.toRegex())

fun findValue(type: FieldType, input: String) =
        matchers[type]?.find(input)?.groupValues?.get(2)

fun isValid(passport: String): Boolean {
    val validFields = matchers
            .map { Pair(it.key, findValue(it.key, passport)) }
            .mapNotNull {
                if (isValid(it.first, it.second))
                    it.first
                else
                    null
            }

    val hasRequired = validFields.containsAll(
            listOf(byr, iyr, eyr, hcl, ecl, pid)
    )
    val hasHgt = validFields.contains(hgtIn) || validFields.contains(hgtCm)

    return hasRequired && hasHgt
}

fun isValid(type: FieldType, value: String?): Boolean {
    if (value == null)
        return false

    return when (type) {
        byr -> value.toInt() in 1920..2002
        iyr -> value.toInt() in 2010..2020
        eyr -> value.toInt() in 2020..2030
        hgtCm -> value.toInt() in 150..193
        hgtIn -> value.toInt() in 59..76
        hcl,ecl,pid,cid -> true
    }
}

fun main(args: Array<String>) {
    println(File("./src/main/resources/day4Input.txt")
            .readText()
            .split("\n\n")
            .map {
                it.split("\n").joinToString("") { line -> line.plus(" ") }
            }
            .map { isValid(it) }
            .filter { it }
            .count())
}



