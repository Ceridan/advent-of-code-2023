package aoc2023

class Day14 {
    fun part1(input: String): Long {
        val (platform, rows, cols) = parseInput(input)
        val (_, totalSum) = tiltPlatformNorth(platform, rows, cols)
        return totalSum
    }

    fun part2(input: String, cycles: Int): Long {
        val (initialPlatform, rows, cols) = parseInput(input)

        var platform = initialPlatform
        var score = 0L
        var cycle = 1
        val cycleScores = mutableListOf<Long>()
        val loopCheckSize = 20
        while (cycle <= cycles) {
            for (dir in listOf("north", "west", "south", "east")) {
                val (newPlatform, newScore) = when (dir) {
                    "north" -> tiltPlatformNorth(platform, rows, cols)
                    "west" -> tiltPlatformWest(platform, rows, cols)
                    "south" -> tiltPlatformSouth(platform, rows, cols)
                    "east" -> tiltPlatformEast(platform, rows, cols)
                    else -> throw IllegalArgumentException("Wrong direction")
                }
                platform = newPlatform
                score = newScore
            }

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

            cycle += 1
        }

        return score
    }

    private fun tiltPlatformNorth(platform: Map<Point, Char>, rows: Int, cols: Int): Pair<Map<Point, Char>, Long> {
        val newPlatform = mutableMapOf<Point, Char>()
        var totalSum = 0L
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
        return newPlatform to totalSum
    }

    private fun tiltPlatformWest(platform: Map<Point, Char>, rows: Int, cols: Int): Pair<Map<Point, Char>, Long> {
        val newPlatform = mutableMapOf<Point, Char>()
        var totalSum = 0L
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
        return newPlatform to totalSum
    }

    private fun tiltPlatformSouth(platform: Map<Point, Char>, rows: Int, cols: Int): Pair<Map<Point, Char>, Long> {
        val newPlatform = mutableMapOf<Point, Char>()
        var totalSum = 0L
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
        return newPlatform to totalSum
    }

    private fun tiltPlatformEast(platform: Map<Point, Char>, rows: Int, cols: Int): Pair<Map<Point, Char>, Long> {
        val newPlatform = mutableMapOf<Point, Char>()
        var totalSum = 0L
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
        return newPlatform to totalSum
    }

    private fun parseInput(input: String): Triple<Map<Point, Char>, Int, Int> {
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

        return Triple(platform, rows, cols)
    }
}

fun main() {
    val day14 = Day14()
    val input = readInputAsString("day14.txt")

    println("14, part 1: ${day14.part1(input)}")
    println("14, part 2: ${day14.part2(input, cycles = 1000000000)}")
}
