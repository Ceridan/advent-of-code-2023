package aoc2023

class Day14 {
    fun part1(input: String): Int = parseInput(input).tiltNorth()

    fun part2(input: String, cycles: Int): Int {
        val platform = parseInput(input)
        val loopCheckSize = 20
        val cycleScores = mutableListOf<Int>()

        while (cycleScores.size <= cycles) {
            val score = platform.tiltCycle()
            cycleScores.add(score)

            val lastN = cycleScores.takeLast(loopCheckSize)
            val loop = cycleScores.dropLast(loopCheckSize).windowed(loopCheckSize, 1).withIndex()
                .firstOrNull { (_, vals) -> vals == lastN }

            loop?.let {
                val loopStart = it.index
                val loopSize = cycleScores.size - loopCheckSize - loopStart
                val finalIdx = (cycles - loopStart) % loopSize + loopStart - 1
                return cycleScores[finalIdx]
            }
        }

        return cycleScores.last()
    }

    private fun parseInput(input: String): Platform {
        val lines = input.split("\n").filter { it.isNotEmpty() }
        val rows = lines.size
        val cols = lines[0].length
        val platform = mutableMapOf<Point, Char>()

        for (y in lines.indices) {
            for (x in lines[y].indices) {
                val ch = lines[y][x]
                if (ch != '.') platform[y to x] = ch
            }
        }

        return Platform(platform, rows = rows, cols = cols)
    }

    data class Platform(private val initialPlatform: Map<Point, Char>, private val rows: Int, private val cols: Int) {
        private var platform = initialPlatform

        fun tiltCycle(): Int {
            tiltNorth()
            tiltWest()
            tiltSouth()
            return tiltEast()
        }

        fun tiltNorth(): Int {
            val newPlatform = mutableMapOf<Point, Char>()
            var totalSum = 0
            for (x in 0..<cols) {
                var hashRockIdx = -1
                for (y in 0..<rows) {
                    val ch = platform.getOrDefault(y to x, '.')
                    if (ch == '#') {
                        newPlatform[y to x] = ch
                        hashRockIdx = y
                    } else if (ch == 'O') {
                        totalSum += rows - hashRockIdx - 1
                        hashRockIdx += 1
                        newPlatform[hashRockIdx to x] = ch
                    }
                }
            }
            platform = newPlatform
            return totalSum
        }

        private fun tiltWest(): Int {
            val newPlatform = mutableMapOf<Point, Char>()
            var totalSum = 0
            for (y in 0..<rows) {
                var hashRockIdx = -1
                for (x in 0..<cols) {
                    val ch = platform.getOrDefault(y to x, '.')
                    if (ch == '#') {
                        newPlatform[y to x] = ch
                        hashRockIdx = x
                    } else if (ch == 'O') {
                        totalSum += rows - y
                        hashRockIdx += 1
                        newPlatform[y to hashRockIdx] = ch
                    }
                }
            }
            platform = newPlatform
            return totalSum
        }

        private fun tiltSouth(): Int {
            val newPlatform = mutableMapOf<Point, Char>()
            var totalSum = 0
            for (x in 0..<cols) {
                var hashRockIdx = rows
                for (y in rows - 1 downTo 0) {
                    val ch = platform.getOrDefault(y to x, '.')
                    if (ch == '#') {
                        newPlatform[y to x] = ch
                        hashRockIdx = y
                    } else if (ch == 'O') {
                        totalSum += rows - hashRockIdx + 1
                        hashRockIdx -= 1
                        newPlatform[hashRockIdx to x] = ch
                    }
                }
            }
            platform = newPlatform
            return totalSum
        }

        private fun tiltEast(): Int {
            val newPlatform = mutableMapOf<Point, Char>()
            var totalSum = 0
            for (y in 0..<rows) {
                var hashRockIdx = cols
                for (x in cols - 1 downTo 0) {
                    val ch = platform.getOrDefault(y to x, '.')
                    if (ch == '#') {
                        newPlatform[y to x] = ch
                        hashRockIdx = x
                    } else if (ch == 'O') {
                        totalSum += rows - y
                        hashRockIdx -= 1
                        newPlatform[y to hashRockIdx] = ch
                    }
                }
            }
            platform = newPlatform
            return totalSum
        }
    }
}

fun main() {
    val day14 = Day14()
    val input = readInputAsString("day14.txt")

    println("14, part 1: ${day14.part1(input)}")
    println("14, part 2: ${day14.part2(input, cycles = 1000000000)}")
}
