package aoc2023

class Day13 {
    fun part1(input: String): Long {
        val patterns = parseInput(input)
        var mirrorSum = 0L
        for (mirrorPattern in patterns) {
            val rowMirror = calculateReflection(mirrorPattern.pattern) * 100
            val colMirror = calculateReflection(mirrorPattern.transpose())
            mirrorSum += rowMirror + colMirror
        }
        return mirrorSum
    }

    fun part2(input: String): Long {
        return 0L
    }

    private fun calculateReflection(pattern: List<CharArray>): Int {
        val candidateIndexes = pattern.zipWithNext().withIndex()
            .filter { (_, pair) -> pair.first.contentEquals(pair.second) }
            .map { (idx, _) -> idx }

        candidates@ for (idx in candidateIndexes) {
            var i = idx - 1
            var j = idx + 2
            while (i >= 0 && j < pattern.size) {
                if (!pattern[i].contentEquals(pattern[j])) continue@candidates
                i -= 1
                j += 1
            }
            return idx + 1
        }
        return 0
    }

    private fun parseInput(input: String): List<MirrorPattern> {
        val lines = (input + "\n").split('\n')
        val mirrorPatterns = mutableListOf<MirrorPattern>()
        var pattern = mutableListOf<CharArray>()
        for (line in lines) {
            if (line.isNotEmpty()) {
                pattern.add(line.toCharArray())
            } else if (pattern.isNotEmpty()) {
                mirrorPatterns.add(MirrorPattern(pattern))
                pattern = mutableListOf()
            }
        }
        return mirrorPatterns
    }

    class MirrorPattern(val pattern: List<CharArray>) {
        fun transpose(): List<CharArray> {
            val rows = pattern.size
            val cols = pattern[0].size

            return List(cols) { j ->
                CharArray(rows) { i -> pattern[i][j] }
            }
        }
    }
}

fun main() {
    val day13 = Day13()
    val input = readInputAsString("day13.txt")

    println("13, part 1: ${day13.part1(input)}")
    println("13, part 2: ${day13.part2(input)}")
}
