package aoc2023

class Day18 {
    fun part1(input: List<String>): Long {
        val instructions = parseInput(input).map { it.instruction }
        var currentPoint = 0 to 0
        val digSite = mutableListOf(currentPoint)
        for (instruction in instructions) {
            LongRange(1, instruction.distance).forEach { _ ->
                currentPoint += instruction.directionAsPoint()
                digSite.add(currentPoint)
            }
        }
        return calculateInterior(digSite)
    }

    fun part2(input: List<String>): Long {
        val instructions = parseInput(input).map { it.decodeColor() }
        return 0
    }

    private fun calculateInterior(digSite: List<Point>): Long {
        val area = calculateArea(digSite)
        val border = digSite.size.toLong()
        val inner = calculateInnerPoints(border, area)
        return (border + inner).toLong()
    }

    private fun calculateInterior(instructions: List<DigInstruction>): Long {
        val area = calculateArea(instructions)
        val border = instructions.sumOf { it.distance } + 1L
        val inner = calculateInnerPoints(border, area)
        return (border + inner).toLong()
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    private fun calculateArea(digSite: List<Point>): Double {
        val doubleArea = (digSite + listOf(digSite[0])).windowed(2).sumOf { (p1, p2) ->  1L * p1.x * p2.y - p1.y * p2.x }
        return doubleArea / 2.0
    }

    private fun calculateArea(instructions: List<DigInstruction>): Double {
        var detSum = 0L
        var point = Point(0, 0)
        for (instruction in instructions) {
            detSum += when (instruction.direction) {
                'R' -> point.y * (1 - instruction.distance)
                'D' -> point.x * (instruction.distance - 1)
                'L' -> point.y * (instruction.distance - 1)
                'U' -> point.x * (1 - instruction.distance)
                else -> throw IllegalArgumentException("Unknown direction ${instruction.direction}")
            }
            point += instruction.directionAsPoint().scale(instruction.distance.toInt())
        }
        return detSum / 2.0
    }

    // https://en.wikipedia.org/wiki/Pick%27s_theorem
    private fun calculateInnerPoints(borderSize: Long, area: Double): Double = (area - borderSize / 2.0 + 1)

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
        fun directionAsPoint(): Point = when(direction) {
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
            val direction = when(color.last()) {
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
