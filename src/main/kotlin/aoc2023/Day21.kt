package aoc2023

class Day21 {
    fun part1(input: String, steps: Int): Int {
        val (start, grid) = parseInput(input)
        val (reachablePointsCount) = getReachablePoints(grid, start, steps)
        return reachablePointsCount
    }

    fun part2(input: String, steps: Int): Long {
        val (start, grid) = parseInput(input)
        val gridSize = grid.keys.maxOf { it.y } + 1

        val initialSteps = steps % gridSize

        // https://en.wikipedia.org/wiki/Vandermonde_matrix
        // https://en.wikipedia.org/wiki/Cramer%27s_rule
        // Vandermonde matrix:
        //     [ 1 0 0 ]
        // A = [ 1 1 1 ]
        //     [ 1 2 4 ]

        val (b0, b1, b2) = getReachablePoints(
            grid,
            start,
            initialSteps,
            initialSteps + gridSize,
            initialSteps + 2 * gridSize
        )
        val detA = 2
        val detA0 = 2 * b0
        val detA1 = -3 * b0 + 4 * b1 - b2
        val detA2 = b0 - 2 * b1 + b2

        val x0 = detA0 / detA
        val x1 = detA1 / detA
        val x2 = detA2 / detA

        val n = 1L * steps / gridSize
        return x0 + x1 * n + x2 * n * n
    }

    private fun getReachablePoints(grid: Map<Point, Char>, start: Point, vararg steps: Int): List<Int> {
        val gridSize = grid.keys.maxOf { it.y } + 1
        var reachablePoints = setOf(start)
        val partialResults = mutableListOf<Int>()
        val stepSet = steps.toSet()
        repeat(steps.last()) { step ->
            reachablePoints = reachablePoints.flatMap { point ->
                listOf(point + (-1 to 0), point + (0 to -1), point + (1 to 0), point + (0 to 1))
            }
                .filter { point ->
                    val gridPoint = point.toGrid(gridSize)
                    grid[gridPoint] != '#'
                }
                .toSet()

            if ((step + 1) in stepSet) partialResults.add(reachablePoints.size)
        }
        return partialResults
    }

    private fun Point.toGrid(gridSize: Int): Point {
        val gridY = (y % gridSize + gridSize) % gridSize
        val gridX = (x % gridSize + gridSize) % gridSize
        return gridY to gridX
    }

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

    println("21, part 1: ${day21.part1(input, 64)}")
    println("21, part 2: ${day21.part2(input, 26501365)}")
}
