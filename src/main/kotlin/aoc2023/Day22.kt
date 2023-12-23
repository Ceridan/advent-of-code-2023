package aoc2023

import kotlin.math.max
import kotlin.math.min

class Day22 {
    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
        val (landed, supportedBy, _) = fall(bricks)
        val onlySupporterIds = getOnlySupporters(supportedBy)
        return landed.size - onlySupporterIds.size
    }

    fun part2(input: List<String>): Long {
        val bricks = parseInput(input)
        val (landed, supportedBy, _) = fall(bricks)
        val onlySupporterIds = getOnlySupporters(supportedBy)
        var globalMoved = 0L
        for (id in onlySupporterIds) {
            val landedWithoutCurrent = landed.filter { it.id != id }
            val (_, _, moved) = fall(landedWithoutCurrent)
            globalMoved += moved
        }
        return globalMoved
    }

    private fun getOnlySupporters(supportedBy: Map<Int, Set<Int>>): Set<Int> =
        supportedBy.filter { it.value.size == 1 }.flatMap { it.value }.toSet()


    private fun fall(bricks: List<Brick>): Triple<List<Brick>, Map<Int, Set<Int>>, Long> {
        val pile = mutableMapOf<Int, MutableSet<Brick>>()
        val supportedBy = bricks.map { it.id }.associateWith { mutableSetOf<Int>() }
        var moved = 0L
        for (fallBrick in bricks.sortedBy { it.zr.first }) {
            var landedZ = 0
            for (maxZ in pile.keys.filter { it < fallBrick.zr.first }.sortedDescending()) {
                for (landedBrick in pile[maxZ]!!) {
                    if (landedBrick.intersectOnXY(fallBrick)) {
                        supportedBy[fallBrick.id]!!.add(landedBrick.id)
                        landedZ = maxZ + 1
                    }
                }

                if (landedZ != 0) break
            }

            var newLandedBrick = fallBrick
            if (fallBrick.zr.first != landedZ) {
                newLandedBrick = fallBrick.fallTo(landedZ)
                moved++
            }

            if (newLandedBrick.zr.last !in pile) {
                pile[newLandedBrick.zr.last] = mutableSetOf()
            }
            pile[newLandedBrick.zr.last]!!.add(newLandedBrick)
        }

        return Triple(pile.values.flatten(), supportedBy, moved)
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

    data class Brick(val id: Int, val xr: IntRange, val yr: IntRange, val zr: IntRange) {
        fun intersectOnXY(other: Brick): Boolean = xr.rangeIntersect(other.xr) && yr.rangeIntersect(other.yr)

        fun fallTo(z: Int): Brick {
            val deltaZ = zr.first - z
            if (deltaZ == 0) return this

            val newZr = IntRange(zr.first - deltaZ, zr.last - deltaZ)
            return Brick(id, xr = xr, yr = yr, zr = newZr)
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
