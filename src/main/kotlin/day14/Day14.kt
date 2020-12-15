package day14

import readFile
import java.lang.Long.parseLong

interface Command
data class MaskCommand(val mask: String): Command
data class MemoryCommand(val address: Long, val value: Long): Command
data class Memory(val data: MutableMap<Long,Long> = mutableMapOf()) {
  private lateinit var mask: String

  fun process(command: Command) {
    when(command) {
      is MaskCommand -> mask = command.mask
      is MemoryCommand -> store(command.address, command.value)
    }
  }

  private fun store(address: Long, value: Long) {
    data[address] = applyMask(mask, value)
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
  commands.forEach {  memory.process(it) }
  println(
    memory.values()
      .map { it.value }
      .sum())
}

private fun applyMask(mask: String, value: Long): Long {
  val andMaskS = mask.replace('X', '1')
  val andMask = parseLong(andMaskS, 2)
  val orMaskS = mask.replace('X', '0')
  val orMask = parseLong(orMaskS, 2)
  return value and andMask or orMask
}