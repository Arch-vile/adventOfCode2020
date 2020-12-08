package day7

import readFile

data class Row(val color: String, val capacity: List<Pair<String, Int>>)
data class Capacity(val bag: Bag, val amount: Int)
data class Bag(val color: String, var capacity: MutableList<Capacity>) {
  fun getContainedBagCount(): Int {
    return capacity
      .map { it.amount + it.amount * it.bag.getContainedBagCount() }
      .sum()
  }
}

fun main(args: Array<String>) {
  val rows =
    readFile("./src/main/resources/day7Input.txt")
      .map { parse(it) }

  val bags = rows.map { Bag(it.color, mutableListOf()) }
  rows
    .forEachIndexed { index, row ->
      // Warning: Here we trust that the bags are in the same order as the rows in input
      val bag = bags[index]
      val bagsCapacity =
        row.capacity
          .map {
            Capacity(
              bags.find { bag -> bag.color == it.first }!!,
              it.second)
          }

      bag.capacity = bagsCapacity.toMutableList()
    }

  // # Part 1
  println(
    bags
      .map { it.toString() }
      .filter { it.contains("shiny gold") }
      .count() - 1
  )

  // # Part 2
  val ourBag = bags.find { it.color == "shiny gold" }!!
  println(ourBag.getContainedBagCount())
}

fun parse(input: String): Row {
  var regexpForBag = """([^ ]* [^ ]*) bags contain""".toRegex()
  var regexpForContents = """(\d+) ([^ ]* [^ ]*)""".toRegex()

  var color = regexpForBag.find(input)?.groupValues!![1]
  var contents = regexpForContents.findAll(input).toList().flatMap { it.groupValues }
    .drop(1)
    .windowed(2, 3)
    .map { Pair(it[1], it[0].toInt()) }
  return Row(color, contents)
}
