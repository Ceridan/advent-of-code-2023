package aoc2023

class Day14 {
    fun part1(input: String): Long {
        val (rows, cols, platform) = parseInput(input)
        val (_, totalSum) = tiltPlatformNorth(platform, rows, cols)
        return totalSum
    }

    fun part2(input: String, cycles: Int): Long {
        val (rows, cols, platform) = parseInput(input)
        val (pN1, g) = tiltPlatformNorth(platform, rows, cols)
        val (pW1, _) = tiltPlatformWest(pN1, rows, cols)
        val (pS1, _) = tiltPlatformSouth(pW1, rows, cols)
        val (pE1, _) = tiltPlatformEast(pS1, rows, cols)

        val (pN2, sN2) = tiltPlatformNorth(pE1, rows, cols)
        val (pW2, sW2) = tiltPlatformWest(pN2, rows, cols)
        val (pS2, sS2) = tiltPlatformSouth(pW2, rows, cols)
        val (_, sE2) = tiltPlatformEast(pS2, rows, cols)

        val dir = cycles % 4

        return when (dir) {
            0 -> sN2
            1 -> sW2
            2 -> sS2
            else -> sE2
        }
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
                    hashRockIdx = y
                } else if (ch == 'O') {
                    totalSum += rows - y
                    hashRockIdx += 1
                    newPlatform[y to hashRockIdx + x] = ch
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
                    hashRockIdx = y
                } else if (ch == 'O') {
                    totalSum += rows - y
                    hashRockIdx -= 1
                    newPlatform[y to hashRockIdx + x] = ch
                }
            }
        }
        return newPlatform to totalSum
    }

    private fun parseInput(input: String): Triple<Int, Int, Map<Point, Char>> {
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

        return Triple(rows, cols, platform)
    }

    enum class Direction {
        NORTH, WEST, SOUTH, EAST
    }
}

fun main() {
    val day14 = Day14()
    val input = readInputAsString("day14.txt")

    println("14, part 1: ${day14.part1(input)}")
    println("14, part 2: ${day14.part2(input, cycles = 1000000000)}")
}
