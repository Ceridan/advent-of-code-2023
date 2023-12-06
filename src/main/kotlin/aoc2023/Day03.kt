package aoc2023

class Day03 {
    fun part1(input: List<String>): Int {
        val symbols = getSymbolCoords(input)
        val numRegex = "(\\d+)".toRegex()
        var sum = 0
        for (y in input.indices) {
            numRegex.findAll(input[y]).forEach { match ->
                if (getAdjacentCoords(y to match.range).any { symbols.containsKey(it) }) {
                    sum += match.value.toInt()
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val gearNumbers = getSymbolCoords(input)
            .filterValues { it == '*' }
            .keys
            .associateBy({ it }, { mutableSetOf<Int>() })

        val numRegex = "(\\d+)".toRegex()
        for (y in input.indices) {
            numRegex.findAll(input[y]).forEach { match ->
                getAdjacentCoords(y to match.range)
                    .filter { coord -> gearNumbers.containsKey(coord) }
                    .forEach { coord -> gearNumbers[coord]!!.add(match.value.toInt()) }
            }
        }

        return gearNumbers.values
            .filter { it.size == 2 }
            .sumOf { nums -> nums.fold(1L) { mult, num -> num * mult } }
    }

    private fun getSymbolCoords(scheme: List<String>): Map<Pair<Int, Int>, Char> {
        val symbols = mutableMapOf<Pair<Int, Int>, Char>()
        for (y in scheme.indices) {
            val chars = scheme[y].toCharArray()
            for (x in chars.indices) {
                val ch = chars[x]
                if (ch.isDigit() || ch == '.') continue
                symbols[y to x] = ch
            }
        }
        return symbols
    }

    private fun getAdjacentCoords(coord: Pair<Int, IntRange>): Set<Pair<Int, Int>> {
        val adjacent = mutableSetOf<Pair<Int, Int>>()
        val (y, x) = coord
        for (dy in y - 1..y + 1) {
            for (dx in x.first - 1..x.last + 1) {
                adjacent.add(dy to dx)
            }
        }
        return adjacent
    }
}

fun main() {
    val day03 = Day03()
    val input = readInputAsStringList("day03.txt")

    println("03, part 1: ${day03.part1(input)}")
    println("03, part 2: ${day03.part2(input)}")
}
