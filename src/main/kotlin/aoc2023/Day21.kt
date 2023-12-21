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
        val (start, grid) = parseInput(input)
        val gridSize = Pair(grid.keys.maxOf { it.y } + 1, grid.keys.maxOf { it.x } + 1)
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque(listOf(start to 0L))
        val planeStats = mutableMapOf<Point, MutableMap<Point, Long>>()
        while (queue.isNotEmpty()) {
            val (point, step) = queue.removeFirst()
            if (point in visited) continue

            val plane = point.getPlane(gridSize) ?: continue
            val projection = point.toMainPlane(plane, gridSize)
            if (grid[projection] == '#') continue
            visited.add(point)

            if (projection !in planeStats) {
                planeStats[projection] = mutableMapOf()
            }
            planeStats[projection]!![plane] = step

            queue.add(Pair(point + (-1 to 0), step + 1L))
            queue.add(Pair(point + (0 to -1), step + 1L))
            queue.add(Pair(point + (1 to 0), step + 1L))
            queue.add(Pair(point + (0 to 1), step + 1L))
        }

        return planeStats.values.sumOf { pointStats(it, steps) }
    }

    private fun pointStats(planeStats: Map<Point, Long>, steps: Long): Long {
        val mainPlaneSteps = planeStats[0 to 0]!!
        val diffs = planeStats.values.map { planeSteps ->
            if (steps < mainPlaneSteps) return 0L

            val delta = planeSteps - mainPlaneSteps
            val pointsNum = if (delta == 0L) 1L else (steps - mainPlaneSteps) / delta
            pointsNum
        }
        return diffs.sum()
    }

    private fun Int.isEven(): Boolean = (this and 1) == 0
    private fun Long.isEven(): Boolean = (this and 1L) == 0L

    private fun Point.getPlane(gridSize: Point): Point? {
        val (ySize, xSize) = gridSize
        if (y < -ySize || y >= 2*ySize || x < -xSize || x >= 2*xSize) return null

        var planeY = 0
        if (y < 0) planeY = -1
        if (y >= ySize) planeY = 1

        var planeX = 0
        if (x < 0) planeX = -1
        if (x >= xSize) planeX = 1

        return planeY to planeX
    }
    private fun Point.toMainPlane(plane: Point, gridSize: Point): Point {
        if (plane == Pair(0, 0)) return this
        val (ySize, xSize) = gridSize

        var newY = y
        if (y < 0) newY = ySize + y
        if (y >= ySize) newY = y - ySize

        var newX = x
        if (x < 0) newX = xSize + x
        if (x >= xSize) newX = x - xSize

        return newY to newX
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

    println("21, part 1: ${day21.part1(input, 64L)}")
    println("21, part 2: ${day21.part2(input, 26501365L)}")
}
