package aoc2023

import java.io.File
import kotlin.math.abs

typealias Point = Pair<Int, Int>

val Point.y get() = this.first
val Point.x get() = this.second
operator fun Point.plus(other: Point) = Point(y + other.y, x + other.x)
operator fun Point.minus(other: Point) = Point(y - other.y, x - other.x)
fun Point.scale(value: Int) = Point(y * value, x * value)
fun Point.normalize(): Point {
    val newY = if (y == 0) 0 else y / abs(y)
    val newX = if (x == 0) 0 else x / abs(x)
    return Point(newY, newX)
}

fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun readInputAsString(filename: String): String = getFile(filename).readText()

fun readInputAsStringList(filename: String): List<String> = getFile(filename).readLines()

private fun getFile(filename: String): File = File("src/main/resources/aoc2023/${filename}")
