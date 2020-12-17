package day15

fun main(args: Array<String>) {
  val input =
    "19,20,14,0,9,1"
      .split(",")
      .map { it.toInt() }
      .toMutableList()

  for (i in 5..2019) {
    val number = input[i]
    val lastOccurrenceIndex = input.dropLast(1).lastIndexOf(number)
    if (lastOccurrenceIndex != -1) {
      input.add(i - lastOccurrenceIndex)
    } else {
      input.add(0)
    }
  }

  // Part 1
  println(input[2019]);


}