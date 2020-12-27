package day22

import java.io.File

fun main(args: Array<String>) {
  val decks = File("./src/main/resources/day22Input.txt")
    .readText()
    .split("\n\n")

  val deck1 = buildDeck(0, decks).toMutableList()
  val deck2 = buildDeck(1, decks).toMutableList()
  val result = playGame(deck1, deck2)

  println("Player ${result.second} won the game")
  println(
    result.first.reversed()
      .mapIndexed { index, card ->
        (index + 1) * card
      }
      .sum())

}

// Play a game (or sub game) and return winning deck (1 or 2)
fun playGame(deck1O: List<Int>, deck2O: List<Int>): Pair<List<Int>, Int> {
  val deck1 = deck1O.toMutableList()
  val deck2 = deck2O.toMutableList()

  var roundCount = 0
  do {
    val deck1Card = deck1.removeAt(0)
    val deck2Card = deck2.removeAt(0)

    var winner: Int
    if (deck1.size >= deck1Card && deck2.size >= deck2Card) {
      winner = playGame(
        deck1.take(deck1Card),
        deck2.take(deck2Card)).second
    } else {
      winner = if (deck1Card > deck2Card) 1 else 2
    }

    if (winner == 1) {
      deck1.add(deck1Card)
      deck1.add(deck2Card)
    } else {
      deck2.add(deck2Card)
      deck2.add(deck1Card)
    }

    // Too many rounds, maybe the infinite game going on?
    roundCount++
    if(roundCount > 1000)
      return Pair(deck1, 1)

  } while ( deck1.isNotEmpty() && deck2.isNotEmpty())

  return if (deck1.isEmpty()) Pair(deck2, 2) else Pair(deck1, 1)
}


private fun buildDeck(player: Int, decks: List<String>) = decks[player].split("\n").drop(1)
  .map { it.toInt() }
