package day14

import readFile
import utils.subsets
import java.lang.Long.parseLong
import kotlin.math.pow

interface Command
data class MaskCommand(val mask: String) : Command
data class MemoryCommand(val address: Long, val value: Long) : Command
data class Memory(val data: MutableMap<Long, Long> = mutableMapOf()) {
  private lateinit var mask: String

  fun process(command: Command) {
    when (command) {
      is MaskCommand -> mask = command.mask
      is MemoryCommand -> store(command.address, command.value)
    }
  }

  private fun store(address: Long, value: Long) {
    val floatingAddresses = applyFloating(mask, address)
    floatingAddresses
      .forEach {
        data[applyMask(mask, address + it)] = value
      }
  }

  fun values(): Set<Map.Entry<Long, Long>> {
    return data.toMap().entries
  }
}

fun main(args: Array<String>) {
  val maskRegex = """mask = ([01X]*)""".toRegex()
  val memoryRegex = """mem\[(\d*)\] = (\d*)""".toRegex()
  val input = readFile("./src/main/resources/day14Input.txt")

  val commands = input
    .map {
      if (maskRegex.matches(it)) {
        MaskCommand(maskRegex.find(it)!!.groupValues[1])
      } else {
        var groupValues = memoryRegex.find(it)!!.groupValues
        MemoryCommand(groupValues[1].toLong(), groupValues[2].toLong())
      }
    }

  val memory = Memory()
  commands.forEach { memory.process(it) }
  println(memory.values().map { it.value }.sum())
}

private fun applyMask(mask: String, value: Long): Long {
  val orMaskS = mask.replace('X', '0')
  val orMask = parseLong(orMaskS, 2)
  return value or orMask
}

private fun applyFloating(mask: String, address: Long): List<Long> {
  var alterations = mask.reversed()
    .mapIndexedNotNull { index, char ->
      if (char == 'X') {
        var thebit = getBit(address, index)
        if (thebit == '0')
          2L.toDouble().pow(index)
        else
          -1 * 2L.toDouble().pow(index)
      } else
        null
    }

  return subsets(alterations)
    .map { it.sum() }
    .map { it.toLong() }
    .plus(0)
}

fun getBit(address: Long, index: Int): Char {
  // LOL, too tired with this puzzle 🤪
  val asBinary = "000000000000000000000000000000000000000" + address.toString(2)
  return asBinary[asBinary.length - index - 1]
}
