package aoc2023

import kotlin.math.max

class Day23 {
    fun part1(input: String): Int {
        val (grid, start, end) = parseInput(input)
        return dfs(grid, start, end) - 1
    }

    fun part2(input: String): Int {
        val (grid, start, end) = parseInput(input)
        val graph = grid.convertToGraph()
        val compactedGraph = graph.compactGraph()
//        val gridWithoutSlopes = grid.mapValues { (_, ch) ->
//            when (ch) {
//                '^' -> '.'
//                '>' -> '.'
//                'v' -> '.'
//                '<' -> '.'
//                else -> ch
//            }
//        }
//        return dfs(gridWithoutSlopes, start, end) - 1
        return dfs2(compactedGraph, start, end, 0)
    }

    private fun dfs(
        grid: Map<Point, Char>,
        current: Point,
        end: Point,
        steps: Int = 0,
        visited: MutableSet<Point> = mutableSetOf(),
    ): Int {
        if (current == end) return steps + 1
        if (current in visited) return 0

        val ch = grid.getOrDefault(current, '#')
        if (ch == '#') return 0

        return when (ch) {
            '^' ->  dfs(grid, current + (-1 to 0), end, steps + 1, visited)
            '>' ->  dfs(grid, current + (0 to 1), end, steps + 1, visited)
            'v' ->  dfs(grid, current + (1 to 0), end, steps + 1, visited)
            '<' ->  dfs(grid, current + (0 to -1), end, steps + 1, visited)
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

    private fun dfs2(
        graph: Map<Point, Vertex>,
        current: Point,
        end: Point,
        steps: Int,
        visited: MutableSet<Point> = mutableSetOf()
    ): Int {
        if (current == end) return steps
        if (current in visited) return 0

        var longestPath = 0
        visited.add(current)
        for (edge in graph[current]!!.edges) {
            longestPath = max(longestPath, dfs2(graph, edge.v2, end, steps + edge.weight, visited))
        }
        visited.remove(current)
        return longestPath
    }

    private fun Map<Point, Char>.convertToGraph(): Map<Point, Vertex> {
        val graph = mutableMapOf<Point, Vertex>()
        for ((point, ch) in this) {
            if (ch == '#') continue

            val edges = mutableListOf<Edge>()
            for (direction in listOf(Point(-1, 0), Point(0, 1), Point(1, 0), Point(0, -1))) {
                val adj = point + direction
                if (this.getOrDefault(adj, '#') == '#') continue
                edges.add(Edge(point, adj))
            }
            graph[point] = Vertex(point, edges)
        }
        return graph
    }

    private fun Map<Point, Vertex>.compactGraph(): Map<Point, Vertex> {
        val newGraph = this.toMutableMap()
        for (point in this.keys) {
            val vertex = newGraph[point]!!
            if (vertex.edges.size != 2) continue

            val (_, p0, w0) = vertex.edges[0]
            val (_, p1, w1) = vertex.edges[1]
            val prevW0 = newGraph[p0]!!.edges.firstOrNull { it.v2 == p1 }?.weight ?: 0
            val prevW1 = newGraph[p1]!!.edges.firstOrNull { it.v2 == p0 }?.weight ?: 0
            val newEdge0 = Edge(p0, p1, max(w0 + w1, prevW0))
            val newEdge1 = Edge(p1, p0, max(w0 + w1, prevW1))
            newGraph[p0] = Vertex(p0, newGraph[p0]!!.edges.filter { it.v2 != point && it.v2 != p1 } + listOf(newEdge0))
            newGraph[p1] = Vertex(p1, newGraph[p1]!!.edges.filter { it.v2 != point && it.v2 != p0 } + listOf(newEdge1))
            newGraph.remove(point)
        }
        return newGraph
    }

    private fun Map<Point, Vertex>.compactGraph2(): Map<Point, Vertex> {
        val newGraph = this.toMutableMap()
        val sortedPoints = newGraph.keys.sortedWith(compareBy({it.first}, {it.second}))
        val queue = ArrayDeque(sortedPoints)
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (point !in newGraph) continue
            val vertex = newGraph[point]!!
            if (vertex.edges.size != 2) continue

            val (_, p0, w0) = vertex.edges[0]
            val (_, p1, w1) = vertex.edges[1]
            val prevW0 = newGraph[p0]!!.edges.firstOrNull { it.v2 == p1 }?.weight ?: 0
            val prevW1 = newGraph[p1]!!.edges.firstOrNull { it.v2 == p0 }?.weight ?: 0
            val newEdge0 = Edge(p0, p1, max(max(w0, w1) + 1, prevW0))
            val newEdge1 = Edge(p1, p0, max(max(w0, w1) + 1, prevW1))
            newGraph[p0] = Vertex(p0, newGraph[p0]!!.edges.filter { it.v2 != point && it.v2 != p1 } + listOf(newEdge0))
            newGraph[p1] = Vertex(p1, newGraph[p1]!!.edges.filter { it.v2 != point && it.v2 != p0 } + listOf(newEdge1))
            newGraph.remove(point)
            queue.addFirst(p1)
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

    data class Vertex (val point: Point, val edges: List<Edge>)
    data class Edge(val v1: Point, val v2: Point, val weight: Int = 1)
}

fun main() {
    val day23 = Day23()
    val input = readInputAsString("day23.txt")

    println("23, part 1: ${day23.part1(input)}")
    println("23, part 2: ${day23.part2(input)}")
}
