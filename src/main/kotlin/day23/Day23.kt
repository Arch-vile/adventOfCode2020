package day23

fun main(args: Array<String>) {

  val cupSetting = "253149867".windowed(1).map { it.toInt() }

  val cupResults = generateSequence(cupSetting) { cups ->
    val currentCup = cups.first()
    val nextThree = cups.drop(1).take(3)

    val remains = cups.drop(4)
    val destinationLabel =
      remains
        .filter { it < currentCup }
        .maxByOrNull { it }
        ?: remains.maxByOrNull { it }!!
    val destinationIndex = remains.indexOf(destinationLabel)

    val newCups = remains.toMutableList()
    newCups.addAll(destinationIndex + 1, nextThree)
    newCups.add(currentCup)
    newCups
  }

  cupResults.take(101)
    .forEach { println(it) }


}
