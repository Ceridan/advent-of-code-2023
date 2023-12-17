package aoc2023

import java.util.PriorityQueue

class Day17 {
    fun part1(input: String): Int {
        val grid = parseInput(input)
        val maxY = grid.keys.maxOf { it.y }
        val maxX = grid.keys.maxOf { it.x }
        return dijkstra(grid, Point(0, 0), Point(maxY, maxX))
    }

    fun part2(input: String): Int {
        return 0
    }

    private fun dijkstra(grid: Map<Point, Int>, source: Point, target: Point): Int {
        val sourceState = StreakState(source, source, 0)
        val costs = mutableMapOf(sourceState to 0)
        val pq = PriorityQueue<QueueItem>()
        pq.add(QueueItem(sourceState, 0))

        while (pq.isNotEmpty()) {
            val item = pq.poll()
            if (item.state.point == target) return costs[item.state] ?: -1
            val nextItems = calculateNextItems(grid, item)
            for (nextItem in nextItems) {
                val currCost = costs.getOrDefault(nextItem.state, Int.MAX_VALUE)
                if (nextItem.cost < currCost) {
                    costs[nextItem.state] = nextItem.cost
                    pq.add(nextItem)
                }
            }
        }
        return -1
    }

    private fun calculateNextItems(grid: Map<Point, Int>, item: QueueItem): List<QueueItem> {
        val nextItems = mutableListOf<QueueItem>()
        val diff = item.state.point - item.state.prevPoint
        val neighbors = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

        for (neighbor in neighbors) {
            if (neighbor == diff && item.state.streak == 3) continue
            val newPoint = item.state.point + neighbor
            if (newPoint == item.state.prevPoint || !grid.containsKey(newPoint)) continue
            val newStreak = if (neighbor == diff) item.state.streak + 1 else 1
            val newState = StreakState(newPoint, item.state.point, newStreak)
            nextItems.add(QueueItem(newState, item.cost + grid[newPoint]!!))
        }
        return nextItems
    }

    private fun parseInput(input: String): Map<Point, Int> {
        val grid = mutableMapOf<Point, Int>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                grid[y to x] = lines[y][x].digitToInt()
            }
        }
        return grid
    }

    data class QueueItem(val state: StreakState, val cost: Int) : Comparable<QueueItem> {
        override fun compareTo(other: QueueItem): Int {
            return cost.compareTo(other.cost)
        }
    }

    data class StreakState(val point: Point, val prevPoint: Point, val streak: Int)
}

fun main() {
    val day17 = Day17()
    val input = readInputAsString("day17.txt")

    println("17, part 1: ${day17.part1(input)}")
    println("17, part 2: ${day17.part2(input)}")
}
