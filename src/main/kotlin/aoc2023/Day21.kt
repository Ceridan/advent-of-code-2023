package aoc2023

class Day21 {
    fun part1(input: String, steps: Int): Int {
        val (start, grid) = parseInput(input)
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque(listOf(start to 0))
        var counter = 0
        while (queue.isNotEmpty()) {
            val (point, step) = queue.removeFirst()
            if (grid[point] == '#') continue
            if (point in visited) continue
            visited.add(point)

            if (step % 2 == 0) counter++
            if (step == steps) continue

            queue.add(Pair(point + (-1 to 0), step + 1))
            queue.add(Pair(point + (0 to -1), step + 1))
            queue.add(Pair(point + (1 to 0), step + 1))
            queue.add(Pair(point + (0 to 1), step + 1))
        }

        return counter
    }

    fun part2(input: String): Int {
        return 0
    }

    private fun parseInput(input: String): Pair<Point, Map<Point, Char>> {
        val grid = mutableMapOf<Point, Char>()
        var start: Point = 0 to 0
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == 'S') start = y to x
                grid[y to x] = lines[y][x]
            }
        }
        return start to grid
    }
}

fun main() {
    val day21 = Day21()
    val input = readInputAsString("day21.txt")

    println("21, part 1: ${day21.part1(input, 64)}")
    println("21, part 2: ${day21.part2(input)}")
}
