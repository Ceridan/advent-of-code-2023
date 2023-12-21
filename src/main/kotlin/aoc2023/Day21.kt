package aoc2023

class Day21 {
    fun part1(input: String, steps: Long): Long {
        val isEven = steps.isEven()
        val (start, grid) = parseInput(input)
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque(listOf(start to 0L))
        var counter = 0L
        while (queue.isNotEmpty()) {
            val (point, step) = queue.removeFirst()
            if (grid[point] == '#') continue
            if (point in visited) continue
            visited.add(point)

            val isEvenCounter = step.isEven()
            if ((isEven && isEvenCounter) || (!isEven && !isEvenCounter)) counter++
            if (step == steps) continue

            queue.add(Pair(point + (-1 to 0), step + 1L))
            queue.add(Pair(point + (0 to -1), step + 1L))
            queue.add(Pair(point + (1 to 0), step + 1L))
            queue.add(Pair(point + (0 to 1), step + 1L))
        }

        return counter
    }

    fun part2(input: String, steps: Long): Long {
        return 0
    }

    private fun Long.isEven(): Boolean = (this and 1) == 0L

    private fun parseInput(input: String): Pair<Point, Map<Point, Char>> {
        val grid = mutableMapOf<Point, Char>()
        var start: Point = 0 to 0
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == 'S') {
                    start = y to x
                    grid[start] = '.'
                } else {
                    grid[y to x] = lines[y][x]
                }
            }
        }
        return start to grid
    }
}

fun main() {
    val day21 = Day21()
    val input = readInputAsString("day21.txt")

    println("21, part 1: ${day21.part1(input, 64L)}")
    println("21, part 2: ${day21.part2(input, 26501365L)}")
}
