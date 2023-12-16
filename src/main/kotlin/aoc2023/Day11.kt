package aoc2023

import kotlin.math.abs

class Day11 {
    fun part1(input: String): Long = calculateDistances(input, 2)

    fun part2(input: String, expansionModifier: Int): Long = calculateDistances(input, expansionModifier)

    private fun calculateDistances(input: String, expansionModifier: Int = 2): Long {
        val galaxies = parseInput(input, expansionModifier)
        var distances = 0L

        for (i in 0..<galaxies.size - 1) {
            for (j in i + 1..<galaxies.size) {
                val (yi, xi) = galaxies[i]
                val (yj, xj) = galaxies[j]
                distances += abs(yi - yj) + abs(xi - xj)
            }
        }

        return distances
    }

    private fun parseInput(input: String, expansionModifier: Int = 2): List<Point> {
        val lines = input.split('\n')
        val galaxyPoints = mutableListOf<Point>()

        for (y in lines.indices) {
            val chars = lines[y].toCharArray()
            for (x in chars.indices) {
                if (chars[x] == '#') {
                    galaxyPoints.add(y to x)
                }
            }
        }

        val emptyRows = IntRange(0, lines.size - 1).subtract(galaxyPoints.map { it.y }.toSet())
        val emptyCols = IntRange(0, lines[0].length - 1).subtract(galaxyPoints.map { it.x }.toSet())

        return galaxyPoints.map { galaxyPoint ->
            val (y, x) = galaxyPoint
            val rowsBefore = emptyRows.count { it < y }
            val colsBefore = emptyCols.count { it < x }
            Pair(y + rowsBefore * (expansionModifier - 1), x + colsBefore * (expansionModifier - 1))
        }.toList()
    }
}

fun main() {
    val day11 = Day11()
    val input = readInputAsString("day11.txt")

    println("11, part 1: ${day11.part1(input)}")
    println("11, part 2: ${day11.part2(input, 1000000)}")
}
