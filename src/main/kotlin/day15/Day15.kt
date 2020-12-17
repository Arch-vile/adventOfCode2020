package day15

fun main(args: Array<String>) {
  val part1Target = 2020L
  val target = 30000000L
  val input =
    "19,20,14,0,9,1"
      .split(",")
      .map { it.toLong() }
      .toMutableList()

  val numbersToIndex  = mutableMapOf<Long,Long>()
  input.dropLast(1).forEachIndexed {index, number ->
    numbersToIndex[number] = index.toLong()
  }

  var number = input.takeLast(1)[0]
  for (i in input.size-1 until target-1) {
    val lastLocation = numbersToIndex[number]
    numbersToIndex[number] = i
    number = if(lastLocation == null) { 0
    } else { i - lastLocation }

    // Part 1
    if(i == part1Target-2) {
     println(number)
    }
  }

  // Part 2
  println(number)
}