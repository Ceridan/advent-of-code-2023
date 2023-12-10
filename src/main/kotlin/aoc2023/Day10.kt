package aoc2023

typealias Coord = Pair<Int, Int>

class Day10 {
    fun part1(input: String): Int {
        val (start, grid) = parseInput(input)
        var prevCoord = start.selfCoord
        var nextCoord = start.outCoord
        var steps = 1
        while (nextCoord != start.selfCoord) {
            grid[nextCoord]!!.let {
                nextCoord = it.getOutCoord(prevCoord)
                prevCoord = it.selfCoord
                steps += 1
            }
        }
        return steps / 2
    }

    fun part2(input: String): Int {
        return 0
    }

    private fun parseInput(input: String): Pair<Pipe, Map<Coord, Pipe>> {
        var startCoord: Coord = -1 to -1
        val grid = mutableMapOf<Coord, Pipe>()

        val lines = input.split('\n')
        for (y in lines.indices) {
            val chars = lines[y].toCharArray()
            for (x in chars.indices) {
                val coord = y to x
                parsePipe(coord, chars[x])?.let {
                    grid[coord] = it
                }

                if (chars[x] == 'S') {
                    startCoord = coord
                }
            }
        }

        val startPipe = parseStart(startCoord, grid)
        grid[startCoord] = startPipe
        return startPipe to grid
    }

    private fun parseStart(coord: Coord, grid: Map<Coord, Pipe>): Pipe {
        val (y, x) = coord
        val possibleTypes = mutableSetOf('|', '-', 'L', 'J', '7', 'F')

        grid[y to x - 1].let {
            if (it == null || (coord != it.inCoord && coord != it.outCoord)) {
                possibleTypes.removeAll(listOf('-', 'J', '7'))
            }
        }
        grid[y to x + 1].let {
            if (it == null || (coord != it.inCoord && coord != it.outCoord)) {
                possibleTypes.removeAll(listOf('-', 'L', 'F'))
            }
        }
        grid[y - 1 to x].let {
            if (it == null || (coord != it.inCoord && coord != it.outCoord)) {
                possibleTypes.removeAll(listOf('|', 'L', 'J'))
            }
        }
        grid[y + 1 to x].let {
            if (it == null || (coord != it.inCoord && coord != it.outCoord)) {
                possibleTypes.removeAll(listOf('|', '7', 'F'))
            }
        }

        if (possibleTypes.size > 1) throw IllegalArgumentException("Multiple start pipe options")

        return parsePipe(coord, possibleTypes.first())!!
    }

    private fun parsePipe(coord: Coord, type: Char): Pipe? {
        val (y, x) = coord
        return when (type) {
            '|' -> Pipe(type, coord, y + 1 to x, y - 1 to x)
            '-' -> Pipe(type, coord, y to x - 1, y to x + 1)
            'L' -> Pipe(type, coord, y - 1 to x, y to x + 1)
            'J' -> Pipe(type, coord, y - 1 to x, y to x - 1)
            '7' -> Pipe(type, coord, y + 1 to x, y to x - 1)
            'F' -> Pipe(type, coord, y + 1 to x, y to x + 1)
            else -> null
        }
    }

    data class Pipe(val type: Char, val selfCoord: Coord, val inCoord: Coord, val outCoord: Coord) {
        fun getOutCoord(inCoord: Coord) = when (inCoord) {
            this.inCoord -> outCoord
            outCoord -> this.inCoord
            else -> throw IllegalArgumentException("Pipe is not connected to $inCoord")
        }
    }
}

fun main() {
    val day10 = Day10()
    val input = readInputAsString("day10.txt")

    println("10, part 1: ${day10.part1(input)}")
    println("10, part 2: ${day10.part2(input)}")
}
