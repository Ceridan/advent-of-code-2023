package aoc2023

import kotlin.math.min

class Day22 {
    fun part1(input: List<String>): Int {
        val bricks = parseInput(input)
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun parseInput(input: List<String>): List<Brick> = input.map { line ->
        val (p1, p2) = line.split('~').map { it.split(',') }
        val point1 = Point3D(p1[0].toInt(), p1[1].toInt(), p1[2].toInt())
        val point2 = Point3D(p2[0].toInt(), p2[1].toInt(), p2[2].toInt())
        Brick(point1, point2)
    }

    data class Point3D(val x: Int, val y: Int, val z: Int)

    data class Brick(val point1: Point3D, val point2: Point3D) {
        val minZ = min(point1.z, point2.z)

        fun intersectOnZ(other: Brick): Boolean = minZ == other.minZ
    }
}

fun main() {
    val day22 = Day22()
    val input = readInputAsStringList("day22.txt")

    println("22, part 1: ${day22.part1(input)}")
    println("22, part 2: ${day22.part2(input)}")
}
