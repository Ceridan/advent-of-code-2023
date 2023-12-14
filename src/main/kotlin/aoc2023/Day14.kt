package aoc2023

class Day14 {
    fun part1(input: String): Long {
        val (rows, cols, platform) = parseInput(input)
        var totalSum = 0L
        for (x in 0..<cols) {
            var hashRockIdx = rows
            for (y in 0..<rows) {
                if (platform[y][x] == '#') {
                    hashRockIdx = rows - y - 1
                } else if (platform[y][x] == 'O') {
                    totalSum += hashRockIdx
                    hashRockIdx -= 1
                }
            }
        }
        return totalSum
    }

    fun part2(input: String, cycles: Int): Long {
        return 0L
    }

    private fun parseInput(input: String): Triple<Int, Int, List<String>> {
        val lines = input.split("\n").filter { it.isNotEmpty() }
        val rows = lines.size
        val cols = lines[0].length
        return Triple(rows, cols, lines)
    }

    enum class Direction {
        NORTH, WEST, SOUTH, EAST
    }
}

fun main() {
    val day14 = Day14()
    val input = readInputAsString("day14.txt")

    println("14, part 1: ${day14.part1(input)}")
    println("14, part 2: ${day14.part2(input, cycles = 1000000000)}")
}
