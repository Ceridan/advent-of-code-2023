package aoc2023

class Day15 {
    fun part1(input: String): Int = input
        .split(',', '\n')
        .filter { it.isNotEmpty() }
        .sumOf { LensHashMap.getHash(it) }

    fun part2(input: String): Int {
        val instructions = input.split(',', '\n').filter { it.isNotEmpty() }

        for (instruction in instructions) {
            if (instruction.endsWith('-')) {
                LensHashMap.removeLens(instruction.dropLast(1))
            } else {
                val (lens, focalLength) = instruction.split('=')
                LensHashMap.putOrReplaceLens(lens, focalLength.toInt())
            }
        }

        return LensHashMap.calculateFocusingPower()
    }

    object LensHashMap {
        private var counter = 0
        private val boxes = IntRange(0, 255).map { it to mutableMapOf<String, Pair<Int, Int>>() }
            .associateBy({ it.first }, { it.second })

        fun putOrReplaceLens(lens: String, focalLength: Int) {
            val hash = getHash(lens)
            boxes[hash]!!.let {
                val idx = it[lens]?.first ?: counter++
                it[lens] = idx to focalLength
            }
        }

        fun removeLens(lens: String) {
            val hash = getHash(lens)
            boxes[hash]!!.remove(lens)
        }

        fun calculateFocusingPower(): Int = IntRange(0, 255)
            .sumOf { boxId -> calculateBoxFocusingPower(boxId) }

        private fun calculateBoxFocusingPower(boxId: Int): Int = boxes[boxId]!!.values
            .sortedBy { (idxInBox, _) -> idxInBox }
            .map { (_, focalLength) -> focalLength }
            .withIndex()
            .sumOf { (idx, focalLength) -> (boxId + 1) * (idx + 1) * focalLength }


        fun getHash(value: String): Int {
            var hash = 0
            value.forEach { hash = (hash + it.code) * 17 % 256 }
            return hash
        }
    }
}

fun main() {
    val day15 = Day15()
    val input = readInputAsString("day15.txt")

    println("15, part 1: ${day15.part1(input)}")
    println("15, part 2: ${day15.part2(input)}")
}
