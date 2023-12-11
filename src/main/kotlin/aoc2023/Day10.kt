package aoc2023

class Day10 {
    fun part1(input: String): Int {
        val (start, grid) = parseInput(input)
        val loop = findLoop(start, grid)
        return loop.size / 2
    }

    fun part2(input: String): Int {
        val (start, grid) = parseInput(input)
        val loop = findLoop(start, grid)
        val (newGridPoints, newLoopPoints) = expandPoints(input, loop)
        val outside = newLoopPoints.toMutableSet()
        val deque = ArrayDeque(listOf(-1 to -1))
        while (deque.isNotEmpty()) {
            val (y, x) = deque.removeFirst()
            if ((y to x) in outside) continue
            if ((y to x) !in newGridPoints) continue

            outside.add(y to x)
            deque.add(y - 1 to x)
            deque.add(y + 1 to x)
            deque.add(y to x - 1)
            deque.add(y to x + 1)
        }

        return newGridPoints.subtract(outside)
            .count { it.first % 2 == 0 && it.second % 2 == 0 }
    }

    private fun findLoop(start: Pipe, grid: Map<Point, Pipe>): Set<Pipe> {
        val loop = mutableSetOf(start)
        var prevPoint = start.selfPoint
        var nextPoint = start.outPoint
        while (nextPoint != start.selfPoint) {
            grid[nextPoint]!!.let {
                loop.add(it)
                nextPoint = it.getOutPoint(prevPoint)
                prevPoint = it.selfPoint
            }
        }
        return loop
    }

    private fun expandPoints(input: String, loop: Set<Pipe>): Pair<Set<Point>, Set<Point>> {
        val newGridPoints = mutableSetOf<Point>()
        val newLoopPoints = mutableSetOf<Point>()
        val lines = input.split('\n')
        val rows = lines.size
        val cols = lines[0].length

        for (y in -1..rows * 2) {
            for (x in -1..cols * 2) {
                newGridPoints.add(y to x)
            }
        }

        for (pipe in loop) {
            val (y, x) = pipe.selfPoint
            val newY = y * 2
            val newX = x * 2
            newGridPoints.add(newY to newX)
            newLoopPoints.add(newY to newX)

            if (pipe.type in setOf('|', 'L', 'J')) {
                newGridPoints.add(newY - 1 to newX)
                newLoopPoints.add(newY - 1 to newX)
            }

            if (pipe.type in setOf('|', '7', 'F')) {
                newGridPoints.add(newY + 1 to newX)
                newLoopPoints.add(newY + 1 to newX)
            }

            if (pipe.type in setOf('-', 'J', '7')) {
                newGridPoints.add(newY to newX - 1)
                newLoopPoints.add(newY to newX - 1)
            }

            if (pipe.type in setOf('-', 'L', 'F')) {
                newGridPoints.add(newY to newX + 1)
                newLoopPoints.add(newY to newX + 1)
            }
        }

        return newGridPoints to newLoopPoints
    }

    private fun parseInput(input: String): Pair<Pipe, Map<Point, Pipe>> {
        var startPoint: Point = -1 to -1
        val grid = mutableMapOf<Point, Pipe>()

        val lines = input.split('\n')
        for (y in lines.indices) {
            val chars = lines[y].toCharArray()
            for (x in chars.indices) {
                val Point = y to x
                grid[Point] = parsePipe(Point, chars[x])

                if (chars[x] == 'S') {
                    startPoint = Point
                }
            }
        }

        val startPipe = parseStart(startPoint, grid)
        grid[startPoint] = startPipe

        return startPipe to grid
    }

    private fun parseStart(Point: Point, grid: Map<Point, Pipe>): Pipe {
        val (y, x) = Point
        val possibleTypes = mutableSetOf('|', '-', 'L', 'J', '7', 'F')

        grid[y to x - 1].let {
            if (it == null || (Point != it.inPoint && Point != it.outPoint)) {
                possibleTypes.removeAll(listOf('-', 'J', '7'))
            }
        }
        grid[y to x + 1].let {
            if (it == null || (Point != it.inPoint && Point != it.outPoint)) {
                possibleTypes.removeAll(listOf('-', 'L', 'F'))
            }
        }
        grid[y - 1 to x].let {
            if (it == null || (Point != it.inPoint && Point != it.outPoint)) {
                possibleTypes.removeAll(listOf('|', 'L', 'J'))
            }
        }
        grid[y + 1 to x].let {
            if (it == null || (Point != it.inPoint && Point != it.outPoint)) {
                possibleTypes.removeAll(listOf('|', '7', 'F'))
            }
        }

        if (possibleTypes.size > 1) throw IllegalArgumentException("Multiple start pipe options")

        return parsePipe(Point, possibleTypes.first())
    }

    private fun parsePipe(Point: Point, type: Char): Pipe {
        val (y, x) = Point
        return when (type) {
            '|' -> Pipe(type, Point, y + 1 to x, y - 1 to x)
            '-' -> Pipe(type, Point, y to x - 1, y to x + 1)
            'L' -> Pipe(type, Point, y - 1 to x, y to x + 1)
            'J' -> Pipe(type, Point, y - 1 to x, y to x - 1)
            '7' -> Pipe(type, Point, y + 1 to x, y to x - 1)
            'F' -> Pipe(type, Point, y + 1 to x, y to x + 1)
            else -> Pipe(type, Point, y to x, y to x)
        }
    }

    data class Pipe(val type: Char, val selfPoint: Point, val inPoint: Point, val outPoint: Point) {
        fun getOutPoint(inPoint: Point) = when (inPoint) {
            this.inPoint -> outPoint
            outPoint -> this.inPoint
            else -> throw IllegalArgumentException("Pipe is not connected to $inPoint")
        }
    }
}

fun main() {
    val day10 = Day10()
    val input = readInputAsString("day10.txt")

    println("10, part 1: ${day10.part1(input)}")
    println("10, part 2: ${day10.part2(input)}")
}
