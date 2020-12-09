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
    when (type) {
      InstructionType.ACC -> memory.accumulator += value
      InstructionType.JMP -> Unit
      InstructionType.NOP -> Unit
    }

    return when (type) {
      InstructionType.ACC -> 1
      InstructionType.JMP -> value
      InstructionType.NOP -> 1
    }
  }
}

data class Memory(var accumulator: Int = 0)
data class ReturnValue(val value: Int, val code: Int)

data class Computer(
  val instructions: List<Instruction>,
  var memory: Memory = Memory(),
  var pointer: Int = 0) {

  fun run(): ReturnValue {

    val nextInstruction = instructions[pointer]
    if (nextInstruction.isExecuted) {
      return ReturnValue(memory.accumulator, -1)
    }

    pointer += nextInstruction.execute(memory)
    if (pointer == instructions.size) {
      return ReturnValue(memory.accumulator, 0)
    }

    return run()
  }
}

fun main(args: Array<String>) {
  val instructions = instructionsFromFile()

  // Part 1
  val computer = Computer(clone(instructions))
  val output = computer.run()
  println(output)

// Part 2
  part2(instructions)
}


fun part2(instructionsO: List<Instruction>) {
  var nextInstructionToChange = 0
  var instructions = clone(instructionsO)
  while (true) {
    val computer = Computer(instructions)
    var result = computer.run()
    if (result.code == 0) {
      println(result.value)
      break
    }

    instructions = changeInstruction(nextInstructionToChange, clone(instructionsO))
    nextInstructionToChange++
  }
}

fun changeInstruction(instructionIndex: Int, instructions: List<Instruction>): List<Instruction> {
  var instruction = instructions[instructionIndex]

  if (instruction.type == InstructionType.NOP) {
    instruction = instruction.copy(type = InstructionType.JMP)
  } else if (instruction.type == InstructionType.JMP) {
    instruction = instruction.copy(type = InstructionType.NOP)
  }

  var mutableList = instructions.toMutableList()
  mutableList[instructionIndex] = instruction
  return mutableList
}

private fun instructionsFromFile() = readFile("./src/main/resources/day8Input.txt")
  .map { parse(it) }

fun parse(row: String): Instruction {
  val regexp = """([a-z]{3}) ((?:\+|-)\d+)""".toRegex()
  val values = regexp.find(row)?.groupValues!!
  return Instruction(InstructionType.valueOf(values[1].toUpperCase()),
    values[2].toInt()
  )
}

fun clone(instructions: List<Instruction>): List<Instruction> {
  return instructions.map { it.copy() }
}
