package aoc2023

class Day12 {
    fun part1(input: String): Long {
        val (rows, groups) = parseInput(input)
        return rows.zip(groups).sumOf { (r, c) -> calculateArrangements(r, c) }
    }

    fun part2(input: String): Long {
        val (rows, groups) = parseInput(input)
        val expandedRows = rows.map { "$it?".repeat(5).dropLast(1) }
        val expandedGroups = groups.map { group ->
            group.joinToString(separator = ",", postfix = ",").repeat(5).dropLast(1).split(',').map { it.toInt() }
        }
        return expandedRows.zip(expandedGroups).sumOf { (r, c) -> calculateArrangements(r, c) }
    }

    private fun calculateArrangements(
        pattern: String,
        groups: List<Int>,
        rowIdx: Int = 0,
        colIdx: Int = 0,
        cache: MutableMap<Pair<Int, Int>, Long> = mutableMapOf()
    ): Long {
        val hashIdx = pattern.drop(rowIdx).indexOf('#')
        if (colIdx == groups.size) {
            return if (hashIdx == -1) 1L else 0L
        }

        return pattern.drop(rowIdx).windowed(groups[colIdx], 1).withIndex()
            .filter { (i, window) ->
                if (window.any { it == '.' }) return@filter false
                if (hashIdx > -1 && i > hashIdx) return@filter false

                val leftIdx = rowIdx + i
                val leftDot = leftIdx == 0 || pattern[leftIdx - 1] == '.' || pattern[leftIdx - 1] == '?'
                val rightIdx = rowIdx + window.length + i
                val rightDot = (rightIdx == pattern.length) || pattern[rightIdx] == '.' || pattern[rightIdx] == '?'
                leftDot && rightDot
            }
            .sumOf { (i, window) ->
                val newRowIdx = rowIdx + window.length + i + 1
                val newColIdx = colIdx + 1
                cache.getOrPut(newRowIdx to newColIdx) {
                    calculateArrangements(
                        pattern,
                        groups,
                        newRowIdx,
                        newColIdx,
                        cache
                    )
                }
            }
    }

    private fun parseInput(input: String): Pair<List<String>, List<List<Int>>> {
        val rows = mutableListOf<String>()
        val groups = mutableListOf<List<Int>>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (line in lines) {
            val (rowString, groupString) = line.split(' ')
            rows.add(rowString)
            groups.add(groupString.split(',').map { it.toInt() })
        }
        return rows to groups
    }
}

fun main() {
    val day12 = Day12()
    val input = readInputAsString("day12.txt")

    println("12, part 1: ${day12.part1(input)}")
    println("12, part 2: ${day12.part2(input)}")
}
