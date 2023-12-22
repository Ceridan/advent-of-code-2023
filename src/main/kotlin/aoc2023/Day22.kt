package aoc2023

import kotlin.math.max
import kotlin.math.min

class Day22 {
    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
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

        val landed = groundMap.values.flatten()
        val supportedByOne = landed.filter { it.supportedBy.size == 1 }.flatMap { it.supportedBy }.toSet().size
        return landed.size - supportedByOne
    }

    fun part2(input: List<String>): Int {
        return 0
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
