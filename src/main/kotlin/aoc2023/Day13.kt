package aoc2023

class Day13 {
    fun part1(input: String): Long {
        val patterns = parseInput(input)
        var mirrorSum = 0L
        for (mirrorPattern in patterns) {
            val rowMirror = calculateReflection(mirrorPattern.rows) * 100
            val colMirror = calculateReflection(mirrorPattern.cols)
            mirrorSum += rowMirror + colMirror
        }
        return mirrorSum
    }

    fun part2(input: String): Long {
        val patterns = parseInput(input)
        var mirrorSum = 0L
        for (mirrorPattern in patterns) {
            val rowMirror = calculateReflectionWithSmudge(mirrorPattern.rows) * 100
            val colMirror = calculateReflectionWithSmudge(mirrorPattern.cols)
            mirrorSum += rowMirror + colMirror
        }
        return mirrorSum
    }

    private fun calculateReflection(pattern: List<Long>): Int {
        val candidateIndexes = pattern.zipWithNext().withIndex()
            .filter { (_, pair) -> (pair.first xor pair.second) == 0L }
            .map { (idx, _) -> idx }

        candidates@ for (idx in candidateIndexes) {
            val iRange = idx - 1 downTo 0
            val jRange = idx + 2..<pattern.size
            for ((i, j) in iRange.zip(jRange)) {
                if ((pattern[i] xor pattern[j]) > 0L) continue@candidates
            }
            return idx + 1
        }
        return 0
    }

    private fun calculateReflectionWithSmudge(pattern: List<Long>): Int {
        val candidates = pattern.zipWithNext()
            .map { (it.first xor it.second).countOneBits() }
            .withIndex()
            .filter { (_, diff) -> diff <= 1 }

        candidates@ for ((idx, diff) in candidates) {
            var hasSmudge = diff == 1
            val iRange = idx - 1 downTo 0
            val jRange = idx + 2..<pattern.size
            for ((i, j) in iRange.zip(jRange)) {
                val nextDiff = (pattern[i] xor pattern[j]).countOneBits()
                if ((nextDiff > 1) || (nextDiff == 1 && hasSmudge)) continue@candidates
                if (nextDiff == 1) hasSmudge = true
            }
            if (hasSmudge) {
                return idx + 1
            }
        }
        return 0
    }

    private fun parseInput(input: String): List<MirrorPattern> {
        val lines = (input + "\n").split('\n')
        val mirrorPatterns = mutableListOf<MirrorPattern>()
        var pattern = mutableListOf<String>()
        for (line in lines) {
            if (line.isNotEmpty()) {
                val binaryLine = line.replace('.', '0').replace('#', '1')
                pattern.add(binaryLine)
            } else if (pattern.isNotEmpty()) {
                mirrorPatterns.add(MirrorPattern(pattern))
                pattern = mutableListOf()
            }
        }
        return mirrorPatterns
    }

    data class MirrorPattern(val pattern: List<String>) {
        val rows = pattern.map { it.toLong(2) }
        val cols = transpose().map { it.toLong(2) }

        private fun transpose(): List<String> {
            val rows = pattern.size
            val cols = pattern[0].length
            return List(cols) { j -> String(CharArray(rows) { i -> pattern[i][j] }) }
        }
    }
}

fun main() {
    val day13 = Day13()
    val input = readInputAsString("day13.txt")

    println("13, part 1: ${day13.part1(input)}")
    println("13, part 2: ${day13.part2(input)}")
}
