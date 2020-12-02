data class Rule(
        val minCount: Int,
        val maxCount: Int,
        val letter: Char)

fun isValid(rule: Rule, passwd: String): Boolean {
    val onlyGivenLetters = passwd.replace("""[^${rule.letter}]*""".toRegex(), "")
    return onlyGivenLetters.length >= rule.minCount &&
            onlyGivenLetters.length <= rule.maxCount
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

