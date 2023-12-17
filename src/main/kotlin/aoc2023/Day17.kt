package aoc2023

import java.util.*

class Day17 {
    fun part1(input: String): Int {
        val (grid, rows, cols) = parseInput(input)
        return dijkstra(grid, Point(0, 0), Point(rows - 1, cols - 1), 1, 3)
    }

    fun part2(input: String): Int {
        val (grid, rows, cols) = parseInput(input)
        return dijkstra(grid, Point(0, 0), Point(rows - 1, cols - 1), 4, 10)
    }

    private fun dijkstra(grid: Map<Point, Int>, source: Point, target: Point, minStreak: Int, maxStreak: Int): Int {
        val sourceState = StreakState(source, source, 0)
        val costs = mutableMapOf(sourceState to 0)
        val pq = PriorityQueue<QueueItem>()
        pq.add(QueueItem(sourceState, 0))

        while (pq.isNotEmpty()) {
            val item = pq.poll()
            if (item.state.point == target) return costs[item.state] ?: -1
            val nextItems = calculateNextItems(grid, item, minStreak, maxStreak)
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

    private fun calculateNextItems(
        grid: Map<Point, Int>,
        item: QueueItem,
        minStreak: Int,
        maxStreak: Int
    ): List<QueueItem> {
        val nextItems = mutableListOf<QueueItem>()
        val currentDirection = (item.state.point - item.state.prevPoint).normalize()
        val possibleDirections =
            listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)).filter { it != currentDirection.scale(-1) }

        for (direction in possibleDirections) {
            if (direction == currentDirection && item.state.streak == maxStreak) continue
            val newStreak = if (direction == currentDirection) item.state.streak + 1 else minStreak
            val streakDiff = if (direction == currentDirection) 1 else minStreak
            val newPoint = item.state.point + direction.scale(streakDiff)
            if (!grid.containsKey(newPoint)) continue
            val newCost = item.cost + IntRange(1, streakDiff).sumOf {
                val tmpPoint = item.state.point + direction.scale(it)
                grid[tmpPoint]!!
            }
            val newStreakState = StreakState(newPoint, item.state.point, newStreak)
            nextItems.add(QueueItem(newStreakState, newCost))
        }

        return nextItems
    }

    private fun parseInput(input: String): Triple<Map<Point, Int>, Int, Int> {
        val grid = mutableMapOf<Point, Int>()
        val lines = input.split('\n').filter { it.isNotEmpty() }
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                grid[y to x] = lines[y][x].digitToInt()
            }
        }
        return Triple(grid, lines.size, lines[0].length)
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
