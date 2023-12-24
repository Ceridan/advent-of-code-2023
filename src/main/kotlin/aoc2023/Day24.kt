package aoc2023

import kotlin.math.sign

class Day24 {
    fun part1(input: List<String>, area: LongRange): Int {
        val hailstones = parseInput(input)
        var intersections = 0

        for (i in 0..<hailstones.size - 1) {
            for (j in i + 1..<hailstones.size) {
                val h1 = hailstones[i]
                val h2 = hailstones[j]
                val crossPoint = h1.intersect2D(h2)

                if (area.includes(crossPoint) && h1.isFuturePoint2D(crossPoint) && h2.isFuturePoint2D(crossPoint)) {
                    intersections++
                }
            }
        }
        return intersections
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    private fun LongRange.includes(point: Point2D): Boolean = first <= point.x && last >= point.x && first <= point.y && last >= point.y

    private fun parseInput(input: List<String>): List<HailStone> {
        val hailLineRegex = "^ *(-?\\d+), +(-?\\d+), +(-?\\d+) @ +(-?\\d+), +(-?\\d+), +(-?\\d+)$".toRegex()
        return input.map { line ->
            val (x, y, z, dx, dy, dz) = hailLineRegex.find(line)!!.destructured
            val position = Point3D(x.toDouble(), y.toDouble(), z.toDouble())
            val velocity = Point3D(dx.toDouble(), dy.toDouble(), dz.toDouble())
            HailStone(position, velocity)
        }
    }

    data class HailStone(val position: Point3D, val velocity: Point3D) {

        // ax + by + c = 0
        fun calculateGeneralForm(): Triple<Double, Double, Double> {
            val (x1, y1, _) = position
            val (x2, y2, _) = Point3D(position.x + velocity.x, position.y + velocity.y, position.z + velocity.z)

            val a = y2 - y1
            val b = x1 - x2
            val c = y1 * (x2 - x1) - x1 * (y2 - y1)
            return Triple(a, b, c)
        }

        // https://www.cuemath.com/geometry/intersection-of-two-lines/
        fun intersect2D(other: HailStone): Point2D {
            val (a1, b1, c1) = this.calculateGeneralForm()
            val (a2, b2, c2) = other.calculateGeneralForm()

            val x0 = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1)
            val y0 = (c1 * a2 - c2 * a1) / (a1 * b2 - a2 * b1)
            return Point2D(x0, y0)
        }

        fun isFuturePoint2D(point: Point2D): Boolean {
            if (sign( point.x - position.x) != sign(velocity.x)) return false
            if (sign( point.y - position.y) != sign(velocity.y)) return false
            return true
        }
    }

    data class Point2D(val x: Double, val y: Double)
    data class Point3D(val x: Double, val y: Double, val z: Double)
}

fun main() {
    val day24 = Day24()
    val input = readInputAsStringList("day24.txt")

    println("24, part 1: ${day24.part1(input, area = 200000000000000L..400000000000000L)}")
    println("24, part 2: ${day24.part2(input)}")
}
