package aoc2023

import kotlin.math.sign
import com.microsoft.z3.Context
import com.microsoft.z3.Status

class Day24 {
    fun part1(input: List<String>, area: LongRange): Int {
        val hailstones = parseInput(input)
        var intersections = 0

        for (i in 0..<hailstones.size - 1) {
            for (j in i + 1..<hailstones.size) {
                val h1 = hailstones[i]
                val h2 = hailstones[j]
                val crossPoint = h1.intersect2D(h2)

                if (crossPoint.isInRange(area) && h1.isFuturePoint2D(crossPoint) && h2.isFuturePoint2D(crossPoint)) {
                    intersections++
                }
            }
        }
        return intersections
    }

    // https://github.com/Z3Prover/z3
    fun part2(input: List<String>): Long {
        val hailstones = parseInput(input)

        val ctx = Context()
        val solver = ctx.mkSolver()

        // Stone
        val sx = ctx.mkIntConst("sx")
        val sy = ctx.mkIntConst("sy")
        val sz = ctx.mkIntConst("sz")
        val sdx = ctx.mkIntConst("sdx")
        val sdy = ctx.mkIntConst("sdy")
        val sdz = ctx.mkIntConst("sdz")

        // Build equations based on a first few hails
        val limit = 3
        for (i in 0..<limit) {
            val hail = hailstones[i]

            val t = ctx.mkIntConst("t$i")

            // Stone
            val stx = ctx.mkAdd(sx, ctx.mkMul(sdx, t))
            val sty = ctx.mkAdd(sy, ctx.mkMul(sdy, t))
            val stz = ctx.mkAdd(sz, ctx.mkMul(sdz, t))

            // Hail
            val hx = ctx.mkInt(hail.position.x.toLong())
            val hy = ctx.mkInt(hail.position.y.toLong())
            val hz = ctx.mkInt(hail.position.z.toLong())
            val hdx = ctx.mkInt(hail.velocity.x.toLong())
            val hdy = ctx.mkInt(hail.velocity.y.toLong())
            val hdz = ctx.mkInt(hail.velocity.z.toLong())

            val htx = ctx.mkAdd(hx, ctx.mkMul(hdx, t))
            val hty = ctx.mkAdd(hy, ctx.mkMul(hdy, t))
            val htz = ctx.mkAdd(hz, ctx.mkMul(hdz, t))

            solver.add(ctx.mkEq(stx, htx))
            solver.add(ctx.mkEq(sty, hty))
            solver.add(ctx.mkEq(stz, htz))
            solver.add(ctx.mkGt(t, ctx.mkInt(0)))
        }

        if (solver.check() == Status.UNSATISFIABLE) throw IllegalStateException("System of equations can't be solved.")

        val stoneX = solver.model.eval(sx, false).toString().toLong()
        val stoneY = solver.model.eval(sy, false).toString().toLong()
        val stoneZ = solver.model.eval(sz, false).toString().toLong()
        return stoneX + stoneY + stoneZ
    }

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

        // https://www.cuemath.com/geometry/intersection-of-two-lines/
        fun intersect2D(other: HailStone): Point2D {
            val (a1, b1, c1) = this.calculateGeneralForm()
            val (a2, b2, c2) = other.calculateGeneralForm()

            val x0 = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1)
            val y0 = (c1 * a2 - c2 * a1) / (a1 * b2 - a2 * b1)
            return Point2D(x0, y0)
        }

        fun isFuturePoint2D(point: Point2D): Boolean {
            if (sign(point.x - position.x) != sign(velocity.x)) return false
            if (sign(point.y - position.y) != sign(velocity.y)) return false
            return true
        }

        // ax + by + c = 0
        private fun calculateGeneralForm(): Triple<Double, Double, Double> {
            val (x1, y1, _) = position
            val (x2, y2, _) = Point3D(position.x + velocity.x, position.y + velocity.y, position.z + velocity.z)

            val a = y2 - y1
            val b = x1 - x2
            val c = y1 * (x2 - x1) - x1 * (y2 - y1)
            return Triple(a, b, c)
        }
    }

    data class Point2D(val x: Double, val y: Double) {
        fun isInRange(range: LongRange): Boolean =
            range.first <= x && range.last >= x && range.first <= y && range.last >= y
    }

    data class Point3D(val x: Double, val y: Double, val z: Double)
}

fun main() {
    val day24 = Day24()
    val input = readInputAsStringList("day24.txt")

    println("24, part 1: ${day24.part1(input, area = 200000000000000L..400000000000000L)}")
    println("24, part 2: ${day24.part2(input)}")
}
