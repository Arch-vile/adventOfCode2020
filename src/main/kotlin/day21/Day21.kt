package day21

import readFile

typealias Allergen = String
typealias Ingredient = String

fun main(args: Array<String>) {
  var products = readFile("./src/main/resources/day21Input.txt")
    .map {
      """(.*) \(contains ([^\)]*)\)""".toRegex().find(it)!!.groupValues
    }
    .map {
      Pair(it[1].split(" ").toSet(), it[2].split(", ").toSet())
    }


  val ingredientByAllergen = mutableMapOf<Allergen, Ingredient>()

  do {
    val allergens = products.flatMap { it.second }.toSet()

    allergens
      .forEach { allergen ->
        println("Processing allergene $allergen")
        var intersectOfAllIngredients = products
          .filter { it.second.contains(allergen) }
          .map { it.first }
          .reduce { acc, list -> acc.intersect(list) }

        println("Found ingredients $intersectOfAllIngredients")

        if (intersectOfAllIngredients.size == 1) {
          val ingredient = intersectOfAllIngredients.first()
          println("found one $ingredient")
          ingredientByAllergen.put(allergen, ingredient)
          products = products
            .map {
              it.copy(
                it.first.toMutableSet().minus(ingredient),
                it.second.toMutableSet().minus(allergen))
            }
        }
      }
  } while (allergens.isNotEmpty())

  // Part 1
  println(
    products
      .map { it.first.size }
      .sum())

  // Part 2
  println(
    ingredientByAllergen
      .entries
      .sortedBy { it.key }
      .map { it.value }
      .joinToString ( "," )
  )

}
