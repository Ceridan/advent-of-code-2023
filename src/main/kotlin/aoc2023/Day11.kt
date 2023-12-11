package aoc2023

import kotlin.math.abs

typealias Point = Pair<Int, Int>

class Day11 {
    fun part1(input: String): Int {
        val galaxies = parseInput(input).toList()
        var distances = 0

        for (i in 0..<galaxies.size-1) {
            for (j in i+1..<galaxies.size) {
                val (yi, xi) = galaxies[i]
                val (yj, xj) = galaxies[j]
                distances += abs(yi - yj) + abs(xi - xj)
            }
        }

        return distances
    }

    fun part2(input: String): Int {
        return 0
    }


    private fun parseInput(input: String): Set<Point> {
        val lines = input.split('\n')
        val galaxyPoints = mutableSetOf<Point>()

        for (y in lines.indices) {
            val chars = lines[y].toCharArray()
            for (x in chars.indices) {
                if (chars[x] == '#') {
                    galaxyPoints.add(y to x)
                }
            }
        }

        val emptyRows = IntRange(0, lines.size - 1).subtract(galaxyPoints.map { it.first }.toSet())
        val emptyCols = IntRange(0, lines[0].length - 1).subtract(galaxyPoints.map { it.second }.toSet())

        return galaxyPoints.map { galaxyPoint ->
            val (y, x) = galaxyPoint
            val rowsBefore = emptyRows.count { it < y }
            val colsBefore = emptyCols.count { it < x }
            Pair(y + rowsBefore, x + colsBefore)
        }.toSet()
    }
}

fun main() {
    val day11 = Day11()
    val input = readInputAsString("day11.txt")

    println("11, part 1: ${day11.part1(input)}")
    println("11, part 2: ${day11.part2(input)}")
}
