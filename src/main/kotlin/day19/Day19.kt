package day19

import java.io.File


fun main(args: Array<String>) {
  val input = File("./src/main/resources/day19Input.txt")
    .readText()
    .split("\n\n")

  val rulesInput =
    input[0].split("\n")
      .map { it.split(" ") }

  val superMegaRegex = buildReqex(rulesInput)
  val messagesInput = input[1].split("\n")
  val matchingMessages = messagesInput
    .map { superMegaRegex.matches(it) }
    .filter { it }
    .count()

  println(matchingMessages)
}

fun buildReqex(rulesInput: List<List<String>>): Regex {
  val rulesMap  = buildRulesMap(rulesInput)
  var ruleZero = rulesMap[0]!!

  while (hasRuleReferences(ruleZero)) {
      ruleZero = ruleZero
      .flatMap {
        if (isNumber(it)) {
           listOf("(").plus(rulesMap[it.toInt()]!!).plus(")")
        } else {
          listOf(it)
        }
      }
  }

  val regExAsString =
    ruleZero
      .joinToString("")
      .replace("""(\(")|("\))""".toRegex(),"")
  println(regExAsString)
  return regExAsString.toRegex()
}

fun buildRulesMap(rulesInput: List<List<String>>): Map<Int, List<String>> {
  return rulesInput.groupBy(
    { value -> value[0].split(":")[0].toInt() },
    { value -> value.drop(1) })
    .map { it.key to it.value[0] }
    .toMap()
}

fun hasRuleReferences(ruleZero: List<String>): Boolean {
 val numberRegex = """\d+""".toRegex()
  return ruleZero.any { numberRegex.matches(it) }
}

fun isNumber(it: String): Boolean {
  return """\d+""".toRegex().matches(it)
}
