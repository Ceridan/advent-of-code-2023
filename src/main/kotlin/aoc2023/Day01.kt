package aoc2023

import kotlin.math.min

class Day01 {
    fun part1(input: List<String>): Int = input
        .map { line -> line.filter { it.isDigit() } }
        .sumOf { digits -> digits.first().digitToInt() * 10 + digits.last().digitToInt() }

    fun part2(input: List<String>): Int {
        val firstDigits = input.map { line -> findFirstDigit(line) }
        val lastDigits = input.map { line -> findFirstDigit(line, isBackward = true) }
        return firstDigits.zip(lastDigits).sumOf { it.first * 10 + it.second }
    }

    private fun findFirstDigit(line: String, isBackward: Boolean = false): Int {
        val range: IntProgression = if (isBackward) line.length - 1 downTo 0 else line.indices
        for (i in range) {
            if (line[i].isDigit()) {
                return line[i].digitToInt()
            }
            for (j in MIN_KEY_SIZE..MAX_KEY_SIZE) {
                val possibleKey = line.substring(i, min(i + j, line.length))
                if (possibleKey in TEXT_TO_DIGIT) {
                    return TEXT_TO_DIGIT[possibleKey]!!
                }
            }
        }
        return 0
    }

    private companion object {
        val TEXT_TO_DIGIT = mapOf(
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
        val MIN_KEY_SIZE = TEXT_TO_DIGIT.keys.minOf { it.length }
        val MAX_KEY_SIZE = TEXT_TO_DIGIT.keys.maxOf { it.length }
    }
}

fun main() {
    val day01 = Day01()
    val input = readInputAsStringList("day01.txt")

    println("Day 01, part 1: ${day01.part1(input)}")
    println("Day 01, part 2: ${day01.part2(input)}")
}
