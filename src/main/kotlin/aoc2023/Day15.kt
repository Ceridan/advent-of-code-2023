package aoc2023

class Day15 {
    fun part1(input: String): Int = input
        .split(',', '\n')
        .filter { it.isNotEmpty() }
        .sumOf { getHash(it) }

    fun part2(input: String): Int {
        val lensHashMap = IntRange(0, 255).map { mutableListOf<Pair<String, Int>>() }.toMutableList()
        val instructions = input.split(',', '\n').filter { it.isNotEmpty() }

        for (instruction in instructions) {
            val lens = instruction.takeWhile { it != '=' && it != '-' }
            val hash = getHash(lens)
            val lenses = lensHashMap[hash]

            if (instruction.endsWith('-')) {
                lenses.removeIf { it.first == lens }
            } else {
                val focalLength = instruction.split('=')[1].toInt()
                val idx = lenses.indexOfFirst { it.first == lens }
                if (idx == -1) {
                    lenses.add(lens to focalLength)
                } else {
                    lenses[idx] = lens to focalLength
                }
            }
        }

        return lensHashMap
            .withIndex()
            .map { (boxIdx, lenses) ->
                lenses.withIndex()
                    .map { Lens(boxId = boxIdx, slotNumber = it.index + 1, focalLength = it.value.second) }
            }
            .flatten()
            .sumOf { it.calculateFocusPower() }
    }

    private fun getHash(value: String): Int {
        var hash = 0
        value.forEach { hash = (hash + it.code) * 17 % 256 }
        return hash
    }

    data class Lens(val boxId: Int, val slotNumber: Int, val focalLength: Int) {
        fun calculateFocusPower(): Int = (boxId + 1) * slotNumber * focalLength
    }
}

fun main() {
    val day15 = Day15()
    val input = readInputAsString("day15.txt")

    println("15, part 1: ${day15.part1(input)}")
    println("15, part 2: ${day15.part2(input)}")
}
