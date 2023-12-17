package aoc2023

import java.io.File

typealias Point = Pair<Int, Int>

val Point.y get() = this.first
val Point.x get() = this.second
operator fun Point.plus(other: Point) = Point(y + other.y, x + other.x)
operator fun Point.minus(other: Point) = Point(y - other.y, x - other.x)
fun Point.scale(value: Int) = Point(y * value, x * value)

fun readInputAsString(filename: String): String = getFile(filename).readText()

fun readInputAsStringList(filename: String): List<String> = getFile(filename).readLines()

private fun getFile(filename: String): File = File("src/main/resources/aoc2023/${filename}")
