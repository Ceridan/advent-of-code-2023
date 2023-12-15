package aoc2023

class Day15 {
    fun part1(input: String): Int = input
        .split(',', '\n')
        .filter { it.isNotEmpty() }
        .sumOf { getHash(it) }

    fun part2(input: String): Int {
        return 0
    }

    private fun getHash(value: String): Int {
        var hash = 0
        value.forEach { hash = (hash + it.code) * 17 % 256 }
        return hash
    }
}

fun main() {
    val day15 = Day15()
    val input = readInputAsString("day15.txt")

    println("15, part 1: ${day15.part1(input)}")
    println("15, part 2: ${day15.part2(input)}")
}
