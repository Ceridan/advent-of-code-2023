package aoc2023

import kotlin.math.max
import kotlin.math.min

class Day22 {
    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
        val landed = fall(bricks)
        val onlySupporters = getOnlySupporters(landed)
        return landed.size - onlySupporters.size
    }

    fun part2(input: List<String>): Long {
        val bricks = parseInput(input)
        val landed = fall(bricks)
        val bricksMap = landed.associateBy { it.id }
        val supportingMap = landed.map { it.id }.associateWith { mutableSetOf<Int>() }.toMutableMap()
        for (brick in landed) {
            for (id in brick.supportedBy) {
                supportingMap[id]!!.add(brick.id)
            }
        }
//        val supportingMap = landed.map { it.id }.associateWith { mutableSetOf<Int>() }.toMutableMap()
//        for (brick in landed.sortedByDescending { it.zr.last }) {
//            for (id in brick.supportedBy) {
//                supportingMap[id]!!.addAll(supportingMap[brick.id]!!)
//                supportingMap[id]!!.add(brick.id)
//            }
//        }

        val onlySupportersIds = getOnlySupporters(landed).map { it.id }
        var totalFallenSum = 0L
        val cache = mutableMapOf<Pair<Int, Set<Int>>, Long>()
        for (id in onlySupportersIds) {
            totalFallenSum += dfs(bricksMap, supportingMap, id, setOf(id), cache)
        }
        return totalFallenSum
    }

    // 124515 - too high

    private fun dfs(bricksMap: Map<Int, Brick>, supportingMap: Map<Int, Set<Int>>, currentId: Int, fallen: Set<Int> = setOf(), cache: MutableMap<Pair<Int, Set<Int>>, Long> = mutableMapOf()): Long {
        if (supportingMap[currentId]!!.isEmpty()) return 0L
        if ((currentId to fallen) in cache) return cache[currentId to fallen]!!
        if (bricksMap[currentId]!!.supportedBy.subtract(fallen).isNotEmpty()) return 0L

        var fallenSum = 0L
        val newFallen = fallen + supportingMap[currentId]!!
        for (id in supportingMap[currentId]!!) {
            fallenSum += 1L + cache.getOrPut(id to newFallen) { dfs(bricksMap, supportingMap, id, newFallen, cache) }
        }

        return fallenSum
    }

    private fun getOnlySupporters(landed: List<Brick>): List<Brick> {
        val onlySupporterIds = landed.filter { it.supportedBy.size == 1 }.flatMap { it.supportedBy }.toSet()
        return landed.filter { it.id in onlySupporterIds }
    }

    private fun fall(bricks: List<Brick>): List<Brick> {
        val grounds = bricks.filter { it.zr.first == 1 }
        val groundMap = grounds.map { it.zr.last }.toSet()
            .associateWith { maxZ -> grounds.filter { it.zr.last == maxZ }.toMutableList() }.toMutableMap()
        val fall = bricks.filter { it.zr.first != 1 }.sortedBy { it.zr.first }
        for (fallBrick in fall) {
            var supportedBy = mutableSetOf<Int>()
            var minZ = 1
            for (maxZ in groundMap.keys.sortedByDescending { it }) {
                for (groundBrick in groundMap[maxZ]!!) {
                    if (fallBrick.intersectOnXY(groundBrick)) {
                        supportedBy.add(groundBrick.id)
                    }
                }
                if (supportedBy.isNotEmpty()) {
                    minZ = maxZ + 1
                    break
                }
            }
            val newGroundBrick = fallBrick.fallTo(minZ, supportedBy)
            if (groundMap[newGroundBrick.zr.last] == null) {
                groundMap[newGroundBrick.zr.last] = mutableListOf()
            }
            groundMap[newGroundBrick.zr.last]!!.add(newGroundBrick)
        }

        return groundMap.values.flatten()
    }

    private fun parseInput(input: List<String>): List<Brick> = input.withIndex().map { (i, line) ->
        val (p1, p2) = line.split('~').map { it.split(',') }
        val (x1, y1, z1) = p1.map { it.toInt() }
        val (x2, y2, z2) = p2.map { it.toInt() }
        Brick(
            i,
            xr = IntRange(min(x1, x2), max(x1, x2)),
            yr = IntRange(min(y1, y2), max(y1, y2)),
            zr = IntRange(min(z1, z2), max(z1, z2))
        )
    }

    data class Brick(
        val id: Int,
        val xr: IntRange,
        val yr: IntRange,
        val zr: IntRange,
        val supportedBy: List<Int> = listOf()
    ) {
        fun intersectOnXY(other: Brick): Boolean = xr.rangeIntersect(other.xr) && yr.rangeIntersect(other.yr)

        fun fallTo(z: Int, supportedBy: Collection<Int>): Brick {
            val deltaZ = zr.first - z
            val newZr = IntRange(zr.first - deltaZ, zr.last - deltaZ)
            return Brick(id, xr = xr, yr = yr, zr = newZr, supportedBy.toList())
        }

        private fun IntRange.rangeIntersect(other: IntRange): Boolean =
            !(this.first > other.last || this.last < other.first)
    }
}

fun main() {
    val day22 = Day22()
    val input = readInputAsStringList("day22.txt")

    println("22, part 1: ${day22.part1(input)}")
    println("22, part 2: ${day22.part2(input)}")
}
