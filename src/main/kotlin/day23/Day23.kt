package day23

class Cup(val label: Int, var previous: Cup?, var next: Cup?) {
  fun joinWith(joinWith: Cup) {
    next = joinWith
    joinWith.previous = this
  }

  override fun toString() = label.toString()
}


fun main(args: Array<String>) {
  val maxCupLabel = 1000000

  val cupSetting = "253149867".windowed(1)
    .map { it.toInt() }
  .plus(IntRange(10,maxCupLabel).toList())

  var firstCup = buildLinkedList(cupSetting)
  val lookupTable = buildLookupTable(firstCup)

  IntRange(1, 10000000).forEach {
    val firstMoved = firstCup.next!!
    val middleMoved = firstMoved.next!!
    val lastMoved = middleMoved.next!!

    // Fix the references to fill the gap
    firstCup.joinWith(lastMoved.next!!)

    var targetCupLabel =
      targetCupLabel(firstCup, firstMoved.label, middleMoved.label, lastMoved.label, maxCupLabel)

    lastMoved.joinWith(lookupTable[targetCupLabel]!!.next!!)
    lookupTable[targetCupLabel]!!.joinWith(firstMoved)

    firstCup = firstCup.next!!
  }

  var cup1 = lookupTable[1]!!.next!!.label
  var cup2 = lookupTable[1]!!.next!!.next!!.label
  println(cup1)
  println(cup2)
  println(cup1.toLong()*cup2)
}

fun targetCupLabel(indexCup: Cup, first: Int, middle: Int, last: Int, max: Int): Int {
  val cupLabel = indexCup.label
  val movedValues = listOf(first, middle, last)
  var target = cupLabel
  do {
    target--
  }while (movedValues.contains(target))

  if(target > 0)
    return target

  target = max
  while(movedValues.contains(target)){
    target--
  }

  return target
}

fun buildLookupTable(firstCup: Cup): Map<Int, Cup> {
  val map = mutableMapOf<Int, Cup>()

  var currentCup = firstCup
  do {
    map[currentCup.label] = currentCup
    currentCup = currentCup.next!!
  } while (currentCup != firstCup)

  return map
}

fun buildLinkedList(cupSetting: List<Int>): Cup {
  var first = Cup(cupSetting.first(), null, null)
  var previous = first
  for (cupLabel in cupSetting.drop(1)) {
    val latest = Cup(cupLabel, previous, null)
    previous.next = latest
    previous = latest
  }
  first.previous = previous
  previous.next = first

  return first
}
