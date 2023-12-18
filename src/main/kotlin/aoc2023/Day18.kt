package aoc2023

class Day18 {
    fun part1(input: List<String>): Long {
        val instructions = parseInput(input).map { it.instruction }
        var currentPoint = 0 to 0
        val digSite = mutableListOf(currentPoint)
        for (instruction in instructions) {
            LongRange(1, instruction.length).forEach { _ ->
                currentPoint += instruction.direction
                digSite.add(currentPoint)
            }
        }
        return calculateInterior(digSite)
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    private fun calculateInterior(digSite: List<Point>): Long {
        val area = calculateArea(digSite)
        val border = digSite.size.toLong()
        val inner = calculateInnerPoints(border, area)
        return (border + inner).toLong()
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    private fun calculateArea(digSite: List<Point>): Double {
        val doubleArea = (digSite + listOf(digSite[0])).windowed(2).sumOf { (p1, p2) ->  1L * p1.x * p2.y - p1.y * p2.x }
        return doubleArea / 2.0
    }

    // https://en.wikipedia.org/wiki/Pick%27s_theorem
    private fun calculateInnerPoints(borderSize: Long, area: Double): Double = (area - borderSize / 2.0 + 1)

    private fun parseInput(input: List<String>): List<ColoredDigInstruction> {
        val instructions = mutableListOf<ColoredDigInstruction>()
        for (instruction in input) {
            val (dir, length, color) = instruction.split(' ')
            val direction = when (dir) {
                "U" -> -1 to 0
                "L" -> 0 to -1
                "D" -> 1 to 0
                "R" -> 0 to 1
                else -> throw IllegalArgumentException("Unknown direction $dir")
            }
            val digInstruction = DigInstruction(direction, length.toLong())
            instructions.add(ColoredDigInstruction(digInstruction, color.drop(1).dropLast(1)))
        }
        return instructions
    }

    data class DigInstruction(val direction: Point, val length: Long)
    data class ColoredDigInstruction(val instruction: DigInstruction, val color: String)
}

fun main() {
    val day18 = Day18()
    val input = readInputAsStringList("day18.txt")

    println("18, part 1: ${day18.part1(input)}")
    println("18, part 2: ${day18.part2(input)}")
}
