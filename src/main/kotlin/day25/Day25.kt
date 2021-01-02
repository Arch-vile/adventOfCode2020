package day25

fun main(args: Array<String>) {
  val publicKeyCard = 8987316
  val publicKeyDoor = 14681524
  val initialSubjectNumber = 7

  val loopSizeCard = calculateLoopSize(publicKeyCard, initialSubjectNumber)
  runHandshake(publicKeyDoor)
    .take(loopSizeCard)
    .last().let { println(it.second) }
}

fun calculateLoopSize(publicKey: Int, subjectNumber: Int): Int {
  val seq = runHandshake(subjectNumber)
  return seq.takeWhile { it.second != publicKey.toLong() }
    .last()
    .first + 1
}

private fun runHandshake(subjectNumber: Int): Sequence<Pair<Int, Long>> {
  val seq = generateSequence(Pair(0, 1L)) {
    Pair(it.first + 1, (it.second * subjectNumber) % 20201227L)
  }
  return seq.drop(1)
}
