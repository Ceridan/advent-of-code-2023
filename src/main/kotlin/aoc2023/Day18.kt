package aoc2023

class Day18 {
    fun part1(input: List<String>): Int {
        val instructions = parseInput(input)
        var currentPoint = 0 to 0
        val digSite = mutableSetOf(currentPoint)
        for (instruction in instructions) {
            IntRange(1, instruction.length).forEach { _ ->
                currentPoint += instruction.direction
                digSite.add(currentPoint)
            }
        }
        return calculateInterior(digSite)
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    private fun calculateInterior(digSite: Set<Point>): Int {
        val minY = digSite.minOf { it.y } - 1
        val maxY = digSite.maxOf { it.y } + 1
        val minX = digSite.minOf { it.x } - 1
        val maxX = digSite.maxOf { it.x } + 1
        val queue = ArrayDeque(listOf(minY to minX))
        val visited = mutableSetOf<Point>()

        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (point in visited) continue
            if (point in digSite) continue
            visited.add(point)

            for (dir in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
                val nextPoint = point + dir
                if (nextPoint.y in minY..maxY && nextPoint.x in minX..maxX) {
                    queue.add(nextPoint)
                }
            }
        }

        return (maxY - minY + 1) * (maxX - minX + 1) - visited.size
    }

    private fun parseInput(input: List<String>): List<DigInstruction> {
        val instructions = mutableListOf<DigInstruction>()
        for (instruction in input) {
            val (dir, length, color) = instruction.split(' ')
            val direction = when (dir) {
                "U" -> -1 to 0
                "L" -> 0 to -1
                "D" -> 1 to 0
                "R" -> 0 to 1
                else -> throw IllegalArgumentException("Unknown direction $dir")
            }
            instructions.add(DigInstruction(direction, length.toInt(), color.drop(1).dropLast(1)))
        }
        return instructions
    }

    data class DigInstruction(val direction: Point, val length: Int, val color: String)
}

fun main() {
    val day18 = Day18()
    val input = readInputAsStringList("day18.txt")

    println("18, part 1: ${day18.part1(input)}")
    println("18, part 2: ${day18.part2(input)}")
}
