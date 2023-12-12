package aoc2023

class Day12 {
    fun part1(input: String): Long {
        val (rows, codes) = parseInput(input)
        return rows.zip(codes).sumOf { (r, c) -> calculateArrangements(r, c) }
    }

    fun part2(input: String): Long {
        return 0
    }

    private fun calculateArrangements(
        pattern: String,
        codes: List<Int>,
        rowIdx: Int = 0,
        colIdx: Int = 0,
        parts: Map<Int, Int> = mapOf()
    ): Long {
        if (colIdx == codes.size) {
            val arrangement = createArrangement(pattern, parts)
            return if (isValidArrangement(arrangement, pattern, codes)) 1L else 0L
        }

        return pattern.drop(rowIdx).windowed(codes[colIdx], 1).withIndex()
            .filter { (i, window) ->
                if (window.any { it == '.' }) return@filter false

                val leftIdx = rowIdx + i
                val leftDot = leftIdx == 0 || pattern[leftIdx - 1] == '.' || pattern[leftIdx - 1] == '?'
                val rightIdx = rowIdx + window.length + i
                val rightDot = (rightIdx == pattern.length) || pattern[rightIdx] == '.' || pattern[rightIdx] == '?'
                leftDot && rightDot
            }
            .sumOf { (i, window) ->
                calculateArrangements(
                    pattern,
                    codes,
                    rowIdx + window.length + i + 1,
                    colIdx + 1,
                    mapOf(rowIdx + i to window.length) + parts
                )
            }
    }

    private fun createArrangement(pattern: String, parts: Map<Int, Int>): String {
        val chars = ".".repeat(pattern.length).toMutableList()
        parts.forEach { (idx, size) ->
            for (i in idx..<idx + size) {
                chars[i] = '#'
            }
        }
        return chars.joinToString(separator = "")
    }

    private fun isValidArrangement(variant: String, pattern: String, codes: List<Int>): Boolean {
        val rowCodes = variant.split('.').filter { it.isNotEmpty() }.map { it.length }
        if (rowCodes != codes) return false

        pattern.zip(variant).forEach { (p, v) ->
            if (p != '?' && p != v) return false
        }

        return true
    }

    private fun parseInput(input: String): Pair<List<String>, List<List<Int>>> {
        val rows = mutableListOf<String>()
        val codes = mutableListOf<List<Int>>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (line in lines) {
            val (rowString, codeString) = line.split(' ')
            rows.add(rowString)
            codes.add(codeString.split(',').map { it.toInt() })
        }
        return rows to codes
    }
}

fun main() {
    val day12 = Day12()
    val input = readInputAsString("day12.txt")

    println("12, part 1: ${day12.part1(input)}")
    println("12, part 2: ${day12.part2(input)}")
}

