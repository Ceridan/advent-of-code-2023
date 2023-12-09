package aoc2023

class Day09 {
    fun part1(input: List<String>): Int {
        val histories = input.map { line -> line.split(' ').map { it.toInt() } }
        var valuesSum = 0
        for (history in histories) {
            val sequences = mutableListOf(history)
            var prevSequence = history
            while (!prevSequence.all { it == 0 }) {
                val nextSequence = mutableListOf<Int>()
                for (i in 1..<prevSequence.size) {
                    nextSequence.add(prevSequence[i] - prevSequence[i-1])
                }
                sequences.add(nextSequence)
                prevSequence = nextSequence
            }

            var nextElement = 0
            for (i in sequences.size-2 downTo 0) {
                nextElement += sequences[i].last()
            }
            valuesSum += nextElement
        }
        return valuesSum
    }

    fun part2(input: List<String>): Int {
        val histories = input.map { line -> line.split(' ').map { it.toInt() } }
        var valuesSum = 0
        for (history in histories) {
            val sequences = mutableListOf(history)
            var prevSequence = history
            while (!prevSequence.all { it == 0 }) {
                val nextSequence = mutableListOf<Int>()
                for (i in 1..<prevSequence.size) {
                    nextSequence.add(prevSequence[i] - prevSequence[i-1])
                }
                sequences.add(nextSequence)
                prevSequence = nextSequence
            }

            var nextElement = 0
            for (i in sequences.size-2 downTo 0) {
                nextElement = sequences[i].first() - nextElement
            }
            valuesSum += nextElement
        }
        return valuesSum
    }
}

fun main() {
    val day09 = Day09()
    val input = readInputAsStringList("day09.txt")

    println("09, part 1: ${day09.part1(input)}")
    println("09, part 2: ${day09.part2(input)}")
}
