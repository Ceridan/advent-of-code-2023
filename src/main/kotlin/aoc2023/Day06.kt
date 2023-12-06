package aoc2023

class Day06 {
    fun part1(input: List<String>): Int = parseRaces(input)
        .map { race -> (1..race.time).map { t -> (race.time - t) * t }.count { it > race.distance } }
        .fold(1) { acc, r -> acc * r }

    fun part2(input: List<String>): Long {
        val races = parseRaces(input)
        val (time, distance) = concatRaces(races)

        var first = 1L
        while ((time - first) * first <= distance) {
            first += 1
        }

        var last = time - 1L
        while ((time - last) * last <= distance) {
            last -= 1
        }

        return last - first + 1L
    }

    private fun concatRaces(races: List<Race>): Pair<Long, Long> {
        val parser: (List<Int>) -> Long = { nums -> nums.joinToString(separator = "") { it.toString() }.toLong() }
        val time = parser(races.map { it.time })
        val distance = parser(races.map { it.distance })
        return time to distance
    }

    private fun parseRaces(input: List<String>): List<Race> {
        val parser: (String) -> List<Int> = { s -> s.split(' ').drop(1).filter { it.isNotEmpty() }.map { it.toInt() } }
        val times = parser(input[0])
        val distances = parser(input[1])
        return times.zip(distances).map { (t, d) -> Race(time = t, distance = d) }
    }

    data class Race(val time: Int, val distance: Int)
}

fun main() {
    val day06 = Day06()
    val input = readInputAsStringList("day06.txt")

    println("06, part 1: ${day06.part1(input)}")
    println("06, part 2: ${day06.part2(input)}")
}
