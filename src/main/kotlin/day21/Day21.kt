package day21

import readFile

fun main(args: Array<String>) {
  var products = readFile("./src/main/resources/day21Input.txt")
    .map {
      """(.*) \(contains ([^\)]*)\)""".toRegex().find(it)!!.groupValues
    }
    .map {
      Pair(it[1].split(" ").toSet(), it[2].split(", ").toSet())
    }


  do {
    val allergens = products.flatMap { it.second }.toSet()

    allergens
      .forEach { allergene ->
        println("Processing allergene $allergene")
        var intersectOfAllIngredients = products
          .filter { it.second.contains(allergene) }
          .map { it.first }
          .reduce { acc, list -> acc.intersect(list) }

        println("Found ingredients $intersectOfAllIngredients")

        if (intersectOfAllIngredients.size == 1) {
          val ingredient = intersectOfAllIngredients.first()
          println("found one $ingredient")
          products = products
            .map {
              it.copy(
                it.first.toMutableSet().minus(ingredient),
                it.second.toMutableSet().minus(allergene))
            }
        }
      }
  } while (allergens.isNotEmpty())

  println(
    products
      .map { it.first.size }
      .sum())

}
