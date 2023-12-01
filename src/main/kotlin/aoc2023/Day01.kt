package aoc2023

import kotlin.math.min

class Day01 {
    fun part1(input: List<String>): Int = input
        .map { line -> line.filter { it.isDigit() } }
        .sumOf { digits -> digits.first().digitToInt() * 10 + digits.last().digitToInt() }

    fun part2(input: List<String>): Int {
        val textToDigit = mapOf(
            "zero" to 0,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )
        val firstDigits = input.map { line -> findFirstDigit(line, textToDigit) }
        val lastDigits =
            input.map { line -> findFirstDigit(line.reversed(), textToDigit.mapKeys { it.key.reversed() }) }
        return firstDigits.zip(lastDigits).sumOf { it.first * 10 + it.second }
    }

    private fun findFirstDigit(line: String, ttd: Map<String, Int>): Int {
        for ((i, ch) in line.withIndex()) {
            if (ch.isDigit()) {
                return ch.digitToInt()
            }
            for (j in 3..5) {
                val possibleKey = line.substring(i, min(i + j, line.length))
                if (possibleKey in ttd) {
                    return ttd[possibleKey]!!
                }
            }
        }
        return 0
    }
}

fun main() {
    val day01 = Day01()
    val input = readInputAsStringList("day01.txt")

    println("Day 01, part 1: ${day01.part1(input)}")
    println("Day 01, part 2: ${day01.part2(input)}")
}
