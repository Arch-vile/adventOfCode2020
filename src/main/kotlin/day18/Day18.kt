package day18

import readFile

fun main(args: Array<String>) {
  val computations = readFile("./src/main/resources/day18Input.txt")

  val sum = computations
    .map { calculate(it) }
    .sum()

  println(sum)
}

fun calculate(originalCalculation: String): Long {
  var calculation = originalCalculation.replace(" ", "")
  while (hasParenthesis(calculation)) {
    calculation = removeDeepestParenthesis(calculation)
  }
  return calculateSimple(calculation)
}

// Input be like "8 * 3 + 2 * 4"
fun calculateSimple(calculation: String): Long {
  return calculation
    .split("*")
    .map { calculateSimpleSum(it) }
    .reduce { acc, number -> acc * number }
}

// Input be like "3+3+1"
fun calculateSimpleSum(calculation: String): Long {
  return calculation.split("+")
    .map { it.toLong() }
    .sum()
}

// Part be like "+3" or "*4"
fun combine(acc: Long, part: String): Long {
  val operation = part.toCharArray()[0]
  return when (operation) {
    '+' -> acc + part.replace("+", "").toLong()
    '*' -> acc * part.replace("*", "").toLong()
    else -> part.toLong()
  }
}

// "3 + 2 * 4" Will be like 3,+2,*4
fun split(calculation: String): List<String> {
  return calculation
    .replace("+", "%+")
    .replace("*", "%*")
    .split("%")
}

// Takes in "3 + ( 4 + (3 * 2))" and return "3 + (4 * 6)"
fun removeDeepestParenthesis(calculation: String): String {
  val deepest =
    """\([^()]*\)""".toRegex()
      .find(calculation)!!.value

  val simpleCalculation = deepest
    .replace("(", "")
    .replace(")", "")

  val value = calculateSimple(simpleCalculation)
  return calculation.replace(deepest, value.toString())
}

fun hasParenthesis(calculation: String): Boolean {
  return calculation.contains('(')
}
