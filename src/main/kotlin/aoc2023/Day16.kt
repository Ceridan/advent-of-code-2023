package aoc2023

import kotlin.math.max

class Day16 {
    fun part1(input: String): Int {
        val grid = parseInput(input)
        return calculateConfiguration(grid, DirectedPoint(0 to 0, 'E'))
    }

    fun part2(input: String): Int {
        val grid = parseInput(input)
        val maxY = grid.keys.maxOf { it.first }
        val maxX = grid.keys.maxOf { it.second }
        var bestConfigurationScore = 0
        for (y in 0..maxY) {
            bestConfigurationScore =
                max(bestConfigurationScore, calculateConfiguration(grid, DirectedPoint(y to 0, 'E')))
            bestConfigurationScore =
                max(bestConfigurationScore, calculateConfiguration(grid, DirectedPoint(y to maxX, 'W')))
        }
        for (x in 0..maxX) {
            bestConfigurationScore =
                max(bestConfigurationScore, calculateConfiguration(grid, DirectedPoint(0 to x, 'S')))
            bestConfigurationScore =
                max(bestConfigurationScore, calculateConfiguration(grid, DirectedPoint(maxY to x, 'N')))
        }
        return bestConfigurationScore
    }

    private fun calculateConfiguration(grid: Map<Point, Char>, entryBeam: DirectedPoint): Int {
        val energized = mutableSetOf<Point>()
        val known = mutableSetOf<DirectedPoint>()
        val queue = ArrayDeque(listOf(entryBeam))
        while (queue.isNotEmpty()) {
            val beam = queue.removeFirst()
            if (known.contains(beam)) continue
            known.add(beam)

            val (beam1, beam2, beamEnergized) = followBeamUntilSplit(grid, beam)
            energized.addAll(beamEnergized)
            beam1?.let { queue.add(beam1) }
            beam2?.let { queue.add(beam2) }
        }
        return energized.size
    }

    private fun followBeamUntilSplit(
        grid: Map<Point, Char>,
        beam: DirectedPoint
    ): Triple<DirectedPoint?, DirectedPoint?, Set<Point>> {
        val energized = mutableSetOf<Point>()
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
