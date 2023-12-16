package aoc2023

class Day16 {
    fun part1(input: String): Int {
        val grid = parseInput(input)
        var energized = mutableSetOf<Point>()
        val known = mutableSetOf<DirectedPoint>()
        val queue = ArrayDeque(listOf(DirectedPoint(0 to 0, 'E')))
        while (queue.isNotEmpty()) {
            val beam = queue.removeFirst()
            if (known.contains(beam)) continue
            known.add(beam)

            val (beam1, beam2, beamEnergized) = followBeamUntilSplit(grid, beam)
            energized.addAll(beamEnergized)
            beam1?.let { queue.add(beam1) }
            beam2?.let { queue.add(beam2) }
        }
        printEnergized(grid, energized)
        return energized.size
    }

    fun part2(input: String): Int {
        return 0
    }

    private fun printEnergized(grid: Map<Point, Char>, energized: Set<Point>) {
        println()
        val maxY = grid.keys.maxOf { it.first }
        val maxX = grid.keys.maxOf { it.second }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (energized.contains(y to x)) print('#')
                else print(grid[y to x])
            }
            println()
        }
        println()
    }

    private fun followBeamUntilSplit(
        grid: Map<Point, Char>,
        beam: DirectedPoint
    ): Triple<DirectedPoint?, DirectedPoint?, Set<Point>> {
        var energized = mutableSetOf<Point>()
        var current = beam
        while (grid.containsKey(current.point)) {
            energized.add(current.point)
            val cell = grid[current.point]
            if (cell == '.') {
                current = current.move(current.direction)
                continue
            }

            current = when (current.direction) {
                'N' -> when (cell) {
                    '\\' -> current.move('W')
                    '/' -> current.move('E')
                    '|' -> current.move('N')
                    else -> return Triple(current.move('W'), current.move('E'), energized)
                }

                'W' -> when (cell) {
                    '\\' -> current.move('N')
                    '/' -> current.move('S')
                    '-' -> current.move('W')
                    else -> return Triple(current.move('N'), current.move('S'), energized)
                }

                'S' -> when (cell) {
                    '\\' -> current.move('E')
                    '/' -> current.move('W')
                    '|' -> current.move('S')
                    else -> return Triple(current.move('E'), current.move('W'), energized)
                }

                'E' -> when (cell) {
                    '\\' -> current.move('S')
                    '/' -> current.move('N')
                    '-' -> current.move('E')
                    else -> return Triple(current.move('S'), current.move('N'), energized)
                }

                else -> throw IllegalArgumentException("Unkwnon direction ${current.direction}")
            }
        }
        return Triple(null, null, energized)
    }

    private fun parseInput(input: String): Map<Point, Char> {
        val grid = mutableMapOf<Point, Char>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                grid[y to x] = lines[y][x]
            }
        }
        return grid
    }

    data class DirectedPoint(val point: Point, val direction: Char) {
        fun move(newDirection: Char): DirectedPoint {
            val (y, x) = point
            val newY = DIRECTION_TRANSFORMS[newDirection]!!.first + y
            val newX = DIRECTION_TRANSFORMS[newDirection]!!.second + x
            return DirectedPoint(newY to newX, newDirection)
        }

        private companion object {
            val DIRECTION_TRANSFORMS = mapOf(
                'N' to Pair(-1, 0),
                'W' to Pair(0, -1),
                'S' to Pair(1, 0),
                'E' to Pair(0, 1),
            )
        }
    }
}

fun main() {
    val day16 = Day16()
    val input = readInputAsString("day16.txt")

    println("16, part 1: ${day16.part1(input)}")
    println("16, part 2: ${day16.part2(input)}")
}
