package aoc2023

import kotlin.math.max

class Day23 {
    fun part1(input: String): Int {
        val (grid, start, end) = parseInput(input)
        val graph = grid.convertToGraph().compactGraph()
        return dfs(graph, start, end)
    }

    fun part2(input: String): Int {
        val (grid, start, end) = parseInput(input)
        val graph = grid
            .mapValues { (_, ch) -> if (ch in ">v<^") '.' else ch }
            .convertToGraph()
            .compactGraph()
        return dfs(graph, start, end)
    }

    private fun dfs(
        graph: Map<Point, List<Edge>>,
        current: Point,
        end: Point,
        steps: Int = 0,
        visited: MutableSet<Point> = mutableSetOf()
    ): Int {
        if (current == end) return steps
        if (current in visited) return 0

        var longestPath = 0
        visited.add(current)
        for (edge in graph[current]!!) {
            longestPath = max(longestPath, dfs(graph, edge.v2, end, steps + edge.weight, visited))
        }
        visited.remove(current)
        return longestPath
    }

    private fun Map<Point, Char>.convertToGraph(): Map<Point, List<Edge>> {
        val graph = mutableMapOf<Point, List<Edge>>()
        for ((point, ch) in this) {
            if (ch == '#') continue

            val edges = mutableListOf<Edge>()
            val directions = when (ch) {
                '>' -> listOf(Point(0, 1))
                'v' -> listOf(Point(1, 0))
                '<' -> listOf(Point(0, -1))
                '^' -> listOf(Point(-1, 0))
                else -> listOf(Point(-1, 0), Point(0, 1), Point(1, 0), Point(0, -1))
            }

            for (direction in directions) {
                val adj = point + direction
                if (this.getOrDefault(adj, '#') == '#') continue
                edges.add(Edge(point, adj))
            }
            graph[point] = edges
        }
        return graph
    }

    private fun Map<Point, List<Edge>>.compactGraph(): Map<Point, List<Edge>> {
        val newGraph = this.toMutableMap()
        for (point in this.keys) {
            val edges = newGraph[point]!!
            if (edges.size != 2) continue

            val (_, p0, w0) = edges[0]
            val (_, p1, w1) = edges[1]
            val prevW0 = newGraph[p0]!!.firstOrNull { it.v2 == p1 }?.weight ?: 0
            val prevW1 = newGraph[p1]!!.firstOrNull { it.v2 == p0 }?.weight ?: 0
            val newEdge0 = Edge(p0, p1, max(w0 + w1, prevW0))
            val newEdge1 = Edge(p1, p0, max(w0 + w1, prevW1))
            newGraph[p0] = newGraph[p0]!!.filter { it.v2 != point && it.v2 != p1 } + listOf(newEdge0)
            newGraph[p1] = newGraph[p1]!!.filter { it.v2 != point && it.v2 != p0 } + listOf(newEdge1)
            newGraph.remove(point)
        }
        return newGraph
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

    data class Edge(val v1: Point, val v2: Point, val weight: Int = 1)
}

fun main() {
    val day23 = Day23()
    val input = readInputAsString("day23.txt")

    println("23, part 1: ${day23.part1(input)}")
    println("23, part 2: ${day23.part2(input)}")
}
