package aoc2023

import java.util.PriorityQueue
import kotlin.math.min

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
        val costs = mutableMapOf(StreakState(source, Point(0, 0), 0) to 0)
        val path = mutableMapOf<Point, Point>()
        val pq = PriorityQueue<QueueItem>()
        pq.add(QueueItem(source, 0, source, 0))

        while (pq.isNotEmpty()) {
            val item = pq.poll()
            if (item.point == target) return costs[item.toStreakState()] ?: -1
            val nextItems = calculateNextItems(grid, item)
            for (nextItem in nextItems) {
                val currCost = costs.getOrDefault(nextItem.toStreakState(), Int.MAX_VALUE)
                if (nextItem.cost < currCost) {
                    costs[nextItem.toStreakState()] = nextItem.cost
                    pq.add(nextItem)
                }
            }
        }
        return -1
    }

    private fun calculateNextItems(grid: Map<Point, Int>, item: QueueItem): List<QueueItem> {
        val nextItems = mutableListOf<QueueItem>()
        val diff = item.point - item.prevPoint
        val neighbors = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

        for (neighbor in neighbors) {
            if (neighbor == diff && item.streak == 3) continue
            val newPoint = item.point + neighbor
            if (newPoint == item.prevPoint || !grid.containsKey(newPoint)) continue
            val streak = if (neighbor == diff) item.streak + 1 else 1
            nextItems.add(QueueItem(newPoint, item.cost + grid[newPoint]!!, item.point, streak))
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

    data class QueueItem(val point: Point, val cost: Int, val prevPoint: Point, val streak: Int) :
        Comparable<QueueItem> {
        override fun compareTo(other: QueueItem): Int {
            return cost.compareTo(other.cost)
        }

        fun toStreakState() = StreakState(point, prevPoint, streak)
    }

    data class StreakState(val point: Point, val prevPoint: Point, val streak: Int)
}

fun main() {
    val day17 = Day17()
    val input = readInputAsString("day17.txt")

    println("17, part 1: ${day17.part1(input)}")
    println("17, part 2: ${day17.part2(input)}")
}
