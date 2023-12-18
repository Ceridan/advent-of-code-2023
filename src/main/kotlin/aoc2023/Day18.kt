package aoc2023

class Day18 {
    fun part1(input: List<String>): Long {
        val instructions = parseInput(input).map { it.instruction }
        return calculateInterior(instructions)
    }

    fun part2(input: List<String>): Long {
        val instructions = parseInput(input).map { it.decodeColor() }
        return calculateInterior(instructions)
    }

    private fun calculateInterior(instructions: List<DigInstruction>): Long {
        val doubleArea = calculateDoubleArea(instructions)
        val border = instructions.sumOf { it.distance }
        val inner = calculateInnerPoints(border, doubleArea)
        return border + inner
    }

    // https://en.wikipedia.org/wiki/Pick%27s_theorem
    private fun calculateInnerPoints(borderSize: Long, doubleArea: Long): Long = (doubleArea - borderSize + 2) / 2

    // https://en.wikipedia.org/wiki/Shoelace_formula
    private fun calculateDoubleArea(instructions: List<DigInstruction>): Long {
        var detSum = 0L
        var point = Point(0, 0)
        for (instruction in instructions) {
            detSum += when (instruction.direction) {
                'R' -> point.y * (-instruction.distance)
                'D' -> point.x * instruction.distance
                'L' -> point.y * instruction.distance
                'U' -> point.x * (-instruction.distance)
                else -> throw IllegalArgumentException("Unknown direction ${instruction.direction}")
            }
            point += instruction.directionAsPoint().scale(instruction.distance.toInt())
        }
        return detSum
    }

    private fun parseInput(input: List<String>): List<ColoredDigInstruction> {
        val instructions = mutableListOf<ColoredDigInstruction>()
        for (instruction in input) {
            val (direction, distance, color) = instruction.split(' ')
            val digInstruction = DigInstruction(direction.first(), distance.toLong())
            instructions.add(ColoredDigInstruction(digInstruction, color.drop(1).dropLast(1)))
        }
        return instructions
    }

    data class DigInstruction(val direction: Char, val distance: Long) {
        fun directionAsPoint(): Point = when (direction) {
            'R' -> 0 to 1
            'D' -> 1 to 0
            'L' -> 0 to -1
            'U' -> -1 to 0
            else -> throw IllegalArgumentException("Unknown direction $direction")
        }
    }

    data class ColoredDigInstruction(val instruction: DigInstruction, val color: String) {
        fun decodeColor(): DigInstruction {
            val distance = color.drop(1).dropLast(1)
            val direction = when (color.last()) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> throw IllegalArgumentException("Unknown direction code ${color.last()}")
            }
            return DigInstruction(direction, distance.toLong(16))
        }
    }
}

fun main() {
    val day18 = Day18()
    val input = readInputAsStringList("day18.txt")

    println("18, part 1: ${day18.part1(input)}")
    println("18, part 2: ${day18.part2(input)}")
}
