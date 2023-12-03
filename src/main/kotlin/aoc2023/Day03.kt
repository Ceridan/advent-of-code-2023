package aoc2023

class Day03 {
    fun part1(input: List<String>): Int {
        val adjacent = getSymbolSurrounding(input)
        val numRegex = "(\\d+)".toRegex()
        var sum = 0
        for (y in input.indices) {
            numRegex.findAll(input[y]).forEach { match ->
                if (match.range.any { x -> adjacent.contains(y to x) }) {
                    sum += match.value.toInt()
                }
            }

        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val gearNumbers = getGearCoords(input)
            .associateBy({it}, {mutableSetOf<Int>()})
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
            .map { nums -> nums.fold(1) { mult, num -> num * mult} }
            .sum()
    }

    private fun getSymbolSurrounding(scheme: List<String>): Set<Pair<Int, Int>> {
        val surrounding = mutableSetOf<Pair<Int, Int>>()
        for (y in scheme.indices) {
            val chars = scheme[y].toCharArray()
            for (x in chars.indices) {
                if (chars[x].isDigit() || chars[x] == '.') continue
                surrounding.addAll(getAdjacentCoords(y to IntRange(x, x)))
            }
        }
        return surrounding
    }

    private fun getGearCoords(scheme: List<String>): Set<Pair<Int, Int>> {
        val coords = mutableSetOf<Pair<Int, Int>>()
        for (y in scheme.indices) {
            val chars = scheme[y].toCharArray()
            for (x in chars.indices) {
                if (chars[x] == '*') coords.add(y to x)
            }
        }
        return coords
    }

    private fun getAdjacentCoords(coord: Pair<Int, IntRange>): Set<Pair<Int, Int>> {
        val surrounding = mutableSetOf<Pair<Int, Int>>()
        val (y, x) = coord
        for (dy in y-1..y+1) {
            for (dx in x.first -1..x.last +1) {
                surrounding.add(dy to dx)
            }
        }
        return surrounding
    }

}

fun main() {
    val day03 = Day03()
    val input = readInputAsStringList("day03.txt")

    println("03, part 1: ${day03.part1(input)}")
    println("03, part 2: ${day03.part2(input)}")
}
