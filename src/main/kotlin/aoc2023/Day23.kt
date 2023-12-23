package aoc2023

class Day23 {
    fun part1(input: String): Int {
        val (grid, start, end) = parseInput(input)
        return dfs(grid, start, end, 0) - 1
    }

    fun part2(input: String): Int {
        val (grid, start, end) = parseInput(input)
        val gridWithoutSlopes = grid.mapValues { (_, ch) ->
            when (ch) {
                '^' -> '.'
                '>' -> '.'
                'v' -> '.'
                '<' -> '.'
                else -> ch
            }
        }
        return dfs(gridWithoutSlopes, start, end, 0) - 1
    }

    private fun dfs(
        grid: Map<Point, Char>,
        current: Point,
        end: Point,
        steps: Int,
        visited: MutableSet<Point> = mutableSetOf()
    ): Int {
        if (current == end) return steps + 1
        if (current in visited) return 0

        val ch = grid.getOrDefault(current, '#')
        if (ch == '#') return 0

        return when (ch) {
            '^' -> dfs(grid, current + (-1 to 0), end, steps + 1, visited)
            '>' -> dfs(grid, current + (0 to 1), end, steps + 1, visited)
            'v' -> dfs(grid, current + (1 to 0), end, steps + 1, visited)
            '<' -> dfs(grid, current + (0 to -1), end, steps + 1, visited)
            else -> {
                visited.add(current)
                val longestPath = maxOf(
                    dfs(grid, current + (-1 to 0), end, steps + 1, visited),
                    dfs(grid, current + (0 to 1), end, steps + 1, visited),
                    dfs(grid, current + (1 to 0), end, steps + 1, visited),
                    dfs(grid, current + (0 to -1), end, steps + 1, visited)
                )
                visited.remove(current)
                longestPath
            }
        }
    }

    private fun parseInput(input: String): Triple<Map<Point, Char>, Point, Point> {
        val lines = input.split('\n').filter { it.isNotEmpty() }
        val grid = mutableMapOf<Point, Char>()
        var start = 0 to 0
        var end = lines.size - 1 to 0
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (y == 0 && lines[y][x] == '.') {
                    start = 0 to x
                }
                if (y == lines.size - 1 && lines[y][x] == '.') {
                    end = lines.size - 1 to x
                }
                grid[y to x] = lines[y][x]
            }
        }
        return Triple(grid, start, end)
    }
}

fun main() {
    val day23 = Day23()
    val input = readInputAsString("day23.txt")

    println("23, part 1: ${day23.part1(input)}")
    println("23, part 2: ${day23.part2(input)}")
}
