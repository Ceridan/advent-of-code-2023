package aoc2023

typealias Coord = Pair<Int, Int>

class Day10 {
    fun part1(input: String): Int {
        val (start, grid) = parseInput(input)
        val loop = findLoop(start, grid)
        return loop.size / 2
    }

    fun part2(input: String): Int {
        val (start, grid) = parseInput(input)
        val loop = findLoop(start, grid)
        val (newGridCoords, newLoopCoords) = expandCoords(input, loop)
        val outside = newLoopCoords.toMutableSet()
        val deque = ArrayDeque(listOf(-1 to -1))
        while (deque.isNotEmpty()) {
            val (y, x) = deque.removeFirst()
            if ((y to x) in outside) continue
            if ((y to x) !in newGridCoords) continue

            outside.add(y to x)
            deque.addFirst(y - 1 to x)
            deque.addFirst(y + 1 to x)
            deque.addFirst(y to x - 1)
            deque.addFirst(y to x + 1)
        }

        return newGridCoords.subtract(outside)
            .count { it.first % 2 == 0 && it.second % 2 == 0 }
    }

    private fun findLoop(start: Pipe, grid: Map<Coord, Pipe>): Set<Pipe> {
        val loop = mutableSetOf(start)
        var prevCoord = start.selfCoord
        var nextCoord = start.outCoord
        while (nextCoord != start.selfCoord) {
            grid[nextCoord]!!.let {
                loop.add(it)
                nextCoord = it.getOutCoord(prevCoord)
                prevCoord = it.selfCoord
            }
        }
        return loop
    }

    private fun expandCoords(input: String, loop: Set<Pipe>): Pair<Set<Coord>, Set<Coord>> {
        val newGridCoords = mutableSetOf<Coord>()
        val newLoopCoords = mutableSetOf<Coord>()
        val lines = input.split('\n')
        val rows = lines.size
        val cols = lines[0].length

        for (y in -1..rows * 2) {
            for (x in -1..cols * 2) {
                newGridCoords.add(y to x)
            }
        }

        for (pipe in loop) {
            val (y, x) = pipe.selfCoord
            val newY = y * 2
            val newX = x * 2
            newGridCoords.add(newY to newX)
            newLoopCoords.add(newY to newX)

            when (pipe.type) {
                '|' -> {
                    newGridCoords.add(newY - 1 to newX)
                    newGridCoords.add(newY + 1 to newX)
                    newLoopCoords.add(newY - 1 to newX)
                    newLoopCoords.add(newY + 1 to newX)
                }

                '-' -> {
                    newGridCoords.add(newY to newX - 1)
                    newGridCoords.add(newY to newX + 1)
                    newLoopCoords.add(newY to newX - 1)
                    newLoopCoords.add(newY to newX + 1)
                }

                'L' -> {
                    newGridCoords.add(newY - 1 to newX)
                    newGridCoords.add(newY to newX + 1)
                    newLoopCoords.add(newY - 1 to newX)
                    newLoopCoords.add(newY to newX + 1)
                }

                'J' -> {
                    newGridCoords.add(newY - 1 to newX)
                    newGridCoords.add(newY to newX - 1)
                    newLoopCoords.add(newY - 1 to newX)
                    newLoopCoords.add(newY to newX - 1)
                }

                '7' -> {
                    newGridCoords.add(newY to newX - 1)
                    newGridCoords.add(newY + 1 to newX)
                    newLoopCoords.add(newY to newX - 1)
                    newLoopCoords.add(newY + 1 to newX)
                }

                'F' -> {
                    newGridCoords.add(newY to newX + 1)
                    newGridCoords.add(newY + 1 to newX)
                    newLoopCoords.add(newY to newX + 1)
                    newLoopCoords.add(newY + 1 to newX)
                }
            }
        }

        return newGridCoords to newLoopCoords
    }

    private fun parseInput(input: String): Pair<Pipe, Map<Coord, Pipe>> {
        var startCoord: Coord = -1 to -1
        val grid = mutableMapOf<Coord, Pipe>()

        val lines = input.split('\n')
        for (y in lines.indices) {
            val chars = lines[y].toCharArray()
            for (x in chars.indices) {
                val coord = y to x
                grid[coord] = parsePipe(coord, chars[x])

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

        return parsePipe(coord, possibleTypes.first())
    }

    private fun parsePipe(coord: Coord, type: Char): Pipe {
        val (y, x) = coord
        return when (type) {
            '|' -> Pipe(type, coord, y + 1 to x, y - 1 to x)
            '-' -> Pipe(type, coord, y to x - 1, y to x + 1)
            'L' -> Pipe(type, coord, y - 1 to x, y to x + 1)
            'J' -> Pipe(type, coord, y - 1 to x, y to x - 1)
            '7' -> Pipe(type, coord, y + 1 to x, y to x - 1)
            'F' -> Pipe(type, coord, y + 1 to x, y to x + 1)
            else -> Pipe(type, coord, y to x, y to x)
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
