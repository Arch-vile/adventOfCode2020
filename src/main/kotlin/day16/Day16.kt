package day16

import readFile
import utils.intersect
import utils.transpose

data class Field(val name: String, val range1: IntRange, val range2: IntRange) {
  fun accepts(number: Int): Boolean {
    return range1.contains(number) || range2.contains(number)
  }
}

fun main(args: Array<String>) {
  val fields = listOf(
    Field("departure location", 34..269, 286..964),
    Field("departure station", 27..584, 609..973),
    Field("departure platform", 49..135, 155..974),
    Field("departure track", 36..248, 255..954),
    Field("departure date", 50..373, 381..974),
    Field("departure time", 49..454, 472..967),
    Field("arrival location", 33..900, 925..968),
    Field("arrival station", 46..699, 706..965),
    Field("arrival platform", 42..656, 666..967),
    Field("arrival track", 49..408, 425..950),
    Field("class", 30..626, 651..957),
    Field("duration", 43..109, 127..964),
    Field("price", 33..778, 795..952),
    Field("route", 37..296, 315..966),
    Field("row", 28..318, 342..965),
    Field("seat", 33..189, 208..959),
    Field("train", 49..536, 552..968),
    Field("type", 46..749, 772..949),
    Field("wagon", 29..386, 401..954),
    Field("zone", 34..344, 368..954),
  )

  val input = readFile("./src/main/resources/day16Input.txt")
    .drop(25)
    .map { it.split(",") }
    .map { it.map { it.toInt() } }

  // For each number collect all fields that match it
  var numbersToFields = input
    .map { numbers ->
      numbers.map { number ->
        fields
          .filter { it.accepts(number) }
      }
    }

  // Part 1
  println(numbersToFields
    .mapIndexed { lineIndex, line ->
      line.mapIndexedNotNull { fieldIndex, fields ->
        if (fields.isEmpty()) {
          input[lineIndex][fieldIndex]
        } else
          null
      }
    }.map { it.sum() }
    .sum()
  )
  println("--------------------------")

  // Part 2
  // Remove invalid tickets
  var validTickets = numbersToFields
    .filter { ticket ->
      ticket.filter { it.isEmpty() }.count() == 0
    }

  val fieldMatches = transpose(validTickets)
  val phase3 = fieldMatches
    .mapIndexed { index, list -> Pair(index, intersect(list)) }
    // Add a dummy element for the window to work on first element in next step
    .plus(Pair(-1, listOf()))
    .sortedBy { it.second.size }
  val fieldsInOrder = phase3
    .windowed(2, 1)
    .map {
      Pair(it[1].first, it[1].second.subtract(it[0].second))
    }
    .sortedBy { it.first }
    .map { it.second }
    .map { it.toList()[0].name }

  val myTicket = listOf(109, 101, 79, 127, 71, 59, 67, 61, 173, 157, 163, 103, 83, 97, 73, 167, 53, 107, 89, 131).map { it.toLong() }

  val answer = fieldsInOrder.zip(myTicket)
    .map { it }
    .filter { it.first.startsWith("departure") }
    .map { it.second }
    .reduce { acc, number -> acc * number }

  println(answer)
}