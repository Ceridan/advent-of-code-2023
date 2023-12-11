package aoc2023

import java.io.File

typealias Point = Pair<Int, Int>

fun readInputAsString(filename: String): String = getFile(filename).readText()

fun readInputAsStringList(filename: String): List<String> = getFile(filename).readLines()

private fun getFile(filename: String): File = File("src/main/resources/aoc2023/${filename}")
