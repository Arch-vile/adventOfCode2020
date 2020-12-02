package day2p2

import readFile

data class Rule(
        val firstIndex: Int,
        val secondIndex: Int,
        val letter: Char)

fun isValid(rule: Rule, passwd: String): Boolean {
    val isFirstIndexRuleLetter = passwd[rule.firstIndex-1] === rule.letter
    val isSecondIndexRuleLetter = passwd[rule.secondIndex-1] === rule.letter
    return (isFirstIndexRuleLetter && !isSecondIndexRuleLetter) ||
            (!isFirstIndexRuleLetter && isSecondIndexRuleLetter)
}

fun parse(line: String): Pair<Rule, String> {
    val regexp = """(\d*)-(\d*) (.): (.*)""".toRegex()
    val groups = regexp.find(line)?.groupValues!!
    return Pair(
            Rule(
                    groups[1].toInt(),
                    groups[2].toInt(),
                    groups[3].toCharArray()[0]),
            groups[4])
}

fun main(args: Array<String>) {
    println(
            readFile("./src/main/resources/day2Input.txt")
                    .map { parse(it) }
                    .filter { isValid(it.first, it.second) }
                    .count()
    )
}

