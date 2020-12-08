package day8

import readFile

enum class InstructionType {
  NOP, ACC, JMP
}

data class Instruction(
  val type: InstructionType,
  val value: Int,
  var isExecuted: Boolean = false) {

  fun execute(memory: Memory): Int {
    isExecuted = true
    when(type) {
      InstructionType.ACC -> memory.accumulator += value
      InstructionType.JMP -> Unit
      InstructionType.NOP -> Unit
    }

    return when(type) {
      InstructionType.ACC -> 1
      InstructionType.JMP -> value
      InstructionType.NOP -> 1
    }
  }
}

data class Memory(var accumulator: Int = 0)

data class Computer(
  val instructions: List<Instruction>,
  var memory: Memory = Memory(),
  var pointer: Int = 0) {

  fun run(): Int {
    val nextInstruction = instructions[pointer]
    if(nextInstruction.isExecuted) {
      return memory.accumulator
    }

    pointer += nextInstruction.execute(memory)
    return run()
  }
}

fun main(args: Array<String>) {
  val instructions =
    readFile("./src/main/resources/day8Input.txt")
      .map { parse(it) }

  val computer = Computer(instructions)
  val output = computer.run()

  println(output)

}

fun parse(row: String): Instruction {
  val regexp = """([a-z]{3}) ((?:\+|-)\d+)""".toRegex()
  val values = regexp.find(row)?.groupValues!!
  return Instruction(InstructionType.valueOf(values[1].toUpperCase()),
    values[2].toInt()
  )
}
